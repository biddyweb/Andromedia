package org.andromda.translation.ocl.query;

import java.util.Iterator;
import java.util.List;
import org.andromda.core.translation.TranslationUtils;
import org.andromda.translation.ocl.BaseTranslator;
import org.andromda.translation.ocl.node.AFeatureCall;
import org.andromda.translation.ocl.node.ALogicalExpressionTail;
import org.andromda.translation.ocl.node.AParenthesesPrimaryExpression;
import org.andromda.translation.ocl.node.APropertyCallExpression;
import org.andromda.translation.ocl.node.ARelationalExpressionTail;
import org.andromda.translation.ocl.node.AStandardDeclarator;
import org.andromda.translation.ocl.node.PRelationalExpression;
import org.andromda.translation.ocl.syntax.ConcreteSyntaxUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Performs translation to the following: <ul> <li>Hibernate-QL</li> </ul>
 *
 * @author Chad Brandon
 */
public class QueryTranslator
        extends BaseTranslator
{
    /**
     * Contains the select clause of the query.
     */
    private StringBuffer selectClause;

    /**
     * Called by super class to reset any objects.
     */
    public void preProcess()
    {
        super.preProcess();
        this.selectClause = new StringBuffer();
        this.sortedByClause = new StringBuffer();
        this.declaratorCtr = 0;
    }

    /**
     * Stores the name of the initial declarator.
     */
    private short declaratorCtr;

    /**
     * True/false whether or not its the initial declarator. We care about this because we must know what the first one
     * is for differentiating between the first declarator (the beginning of the query) and any other declarators (the
     * connecting associations).
     * @return isInitialDeclarator
     */
    protected boolean isInitialDeclarator()
    {
        boolean isInitialDeclarator = (this.declaratorCtr <= 0);
        this.declaratorCtr++;
        return isInitialDeclarator;
    }

    /**
     * Helps out with 'includesAll', replaces the {1} in the expression fragment when a declarator is encountered (i.e.
     * '| <variable name>')
     * @param declarator
     */
    public void inAStandardDeclarator(AStandardDeclarator declarator)
    {
        final String methodName = "QueryTranslator.inAStandardDeclarator";
        if (logger.isDebugEnabled())
            logger.debug("performing " + methodName + " with declarator --> " + declarator);

        String temp = this.selectClause.toString();

        String declaratorName = ConcreteSyntaxUtils.getVariableDeclarations(declarator)[0].getName();

        // by default we'll assume we're replacing the {1} arg.
        short replacement = 1;
        if (this.isInitialDeclarator())
        {
            // handle differently if its the initial declarator,
            // replacement is {0}
            replacement = 0;
        }

        // now replace {1} reference
        temp = TranslationUtils.replacePattern(temp, String.valueOf(replacement), declaratorName);
        this.selectClause = new StringBuffer(temp);
    }

    /**
     * Override to handle any propertyCall expressions ( i.e. includes( <expression>), select( <expression>), etc.)
     * @param expression
     * @see BaseTranslator#handleTranslationFragment(Object)
     */
    public void inAPropertyCallExpression(APropertyCallExpression expression)
    {
        this.handleTranslationFragment(expression);
    }

    /**
     * Override to handle any featureCall expressions ( i.e. sortedBy( <expression>), etc.)
     * @param expression
     * @see BaseTranslator#handleTranslationFragment(Object)
     */
    public void inAFeatureCall(AFeatureCall expression)
    {
        // don't handle all instances here, since it's handled
        // in the property call expression.
        if (!TranslationUtils.trimToEmpty(expression).matches(OCLPatterns.ALL_INSTANCES))
        {
            this.handleTranslationFragment(expression);
        }
    }

    /**
     * Override to deal with logical 'and, 'or', 'xor, ... expressions.
     * @param logicalExpressionTail
     * @see BaseTranslator#handleTranslationFragment(Object)
     */
    public void inALogicalExpressionTail(ALogicalExpressionTail logicalExpressionTail)
    {
        this.handleTranslationFragment(logicalExpressionTail);
    }

    /**
     * Override to deal with relational ' <, '>', '=', ... expressions.
     * @param relationalExpressionTail
     * @see BaseTranslator#handleTranslationFragment(Object)
     */
    public void inARelationalExpressionTail(ARelationalExpressionTail relationalExpressionTail)
    {
        this.handleTranslationFragment(relationalExpressionTail);
    }

    /**
     * Override to deal with entering parenthesis expressions '( <expression>)'.
     * @param expression
     * @see org.andromda.core.translation.Expression#appendToTranslatedExpression(Object)
     */
    public void inAParenthesesPrimaryExpression(AParenthesesPrimaryExpression expression)
    {
        this.getExpression().appendSpaceToTranslatedExpression();
        this.getExpression().appendToTranslatedExpression("(");
    }

    /**
     * Override to deal with leaving parenthesis expressions '( <expression>)'.
     * @param expression
     * @see org.andromda.core.translation.Expression#appendToTranslatedExpression(Object)
     */
    public void outAParenthesesPrimaryExpression(AParenthesesPrimaryExpression expression)
    {
        this.getExpression().appendSpaceToTranslatedExpression();
        this.getExpression().appendToTranslatedExpression(")");
    }

    /**
     * Checks to see if the replacement is an argument and if so replaces the {index} in the fragment with the
     * 'argument' fragment from the template. Otherwise replaces the {index} with the passed in replacement value.
     *
     * @param fragment    the fragment to perform replacement.
     * @param replacement the replacement string
     * @param index       the index in the string to replace.
     * @return String the fragment with any replacements.
     */
    protected String replaceFragment(String fragment, String replacement, int index)
    {
        fragment = StringUtils.trimToEmpty(fragment);
        replacement = StringUtils.trimToEmpty(replacement);
        // if 'replacement' is an argument use that for the replacement
        // in the fragment
        if (this.isOperationArgument(replacement))
        {
            final String argument = replacement;
            replacement = this.getTranslationFragment("argument");
            replacement = TranslationUtils.replacePattern(replacement, String.valueOf(0), argument);
        }
        fragment = TranslationUtils.replacePattern(fragment, String.valueOf(index), replacement);
        return fragment;
    }

    /**
     * Stores the name of the fragment that maps the tail of the select clause.
     */
    private static final String SELECT_CLAUSE_TAIL = "selectClauseTail";

    /**
     * Stores the name of the fragment that maps to the head of the sortedBy clause.
     */
    private static final String SORTED_BY_CLAUSE_HEAD = "sortedByClauseHead";

    /**
     * Handles any final processing.
     */
    @Override
    public void postProcess()
    {
        // create the final translated expression
        String selectClauseTail = this.getTranslationFragment(SELECT_CLAUSE_TAIL);
        String existingExpression = StringUtils.trimToEmpty(this.getExpression().getTranslatedExpression());

        if (StringUtils.isNotBlank(selectClauseTail) && StringUtils.isNotBlank(existingExpression))
        {
            this.selectClause.append(' ');
            this.selectClause.append(selectClauseTail);
            this.selectClause.append(' ');
        }

        this.getExpression().insertInTranslatedExpression(0, selectClause.toString());

        if (this.sortedByClause.length() > 0)
        {
            this.getExpression().appendSpaceToTranslatedExpression();
            this.getExpression().appendToTranslatedExpression(this.getTranslationFragment(SORTED_BY_CLAUSE_HEAD));
            this.getExpression().appendSpaceToTranslatedExpression();
            this.getExpression().appendToTranslatedExpression(this.sortedByClause);
        }

        // remove any extra space from parenthesis
        this.getExpression().replaceInTranslatedExpression("\\(\\s*", "(");
        this.getExpression().replaceInTranslatedExpression("\\s*\\)", ")");
    }

    /*------------------------- Handler methods ---------------------------------------*/

    /*------------------------- PropertyCallExpression Handler methods ---------------------*/

    /**
     * @param translation
     * @param node
     */
    public void handleSubSelect(String translation, Object node)
    {
        APropertyCallExpression propertyCallExpression = (APropertyCallExpression) node;

        String primaryExpression = ConcreteSyntaxUtils.getPrimaryExpression(propertyCallExpression);

        // set the association which the 'includesAll' indicates (which
        // is the first feature call from the list of feature calls)
        translation = this.replaceFragment(translation, TranslationUtils.trimToEmpty(primaryExpression), 0);

        this.selectClause.append(' ');
        this.selectClause.append(translation);
    }

    /**
     * @param translation
     * @param node
     */
    public void handleIsLike(String translation, Object node)
    {
        APropertyCallExpression propertyCallExpression = (APropertyCallExpression) node;
        List featureCalls = ConcreteSyntaxUtils.getFeatureCalls(propertyCallExpression);

        List params = ConcreteSyntaxUtils.getParameters((AFeatureCall) featureCalls.get(0));

        translation = this.replaceFragment(translation, TranslationUtils.deleteWhitespace(params.get(0)), 0);
        translation = this.replaceFragment(translation, TranslationUtils.deleteWhitespace(params.get(1)), 1);

        if (StringUtils.isNotBlank(this.getExpression().getTranslatedExpression()))
        {
            this.getExpression().appendSpaceToTranslatedExpression();
        }
        this.getExpression().appendToTranslatedExpression(translation);
    }

    /**
     * @param translation
     * @param node
     */
    public void handleSelect(String translation, Object node)
    {
        this.selectClause.append(translation);
    }

    /**
     * @param translation
     * @param node
     */
    public void handleIncludes(String translation, Object node)
    {
        APropertyCallExpression propertyCallExpression = (APropertyCallExpression) node;
        List featureCalls = ConcreteSyntaxUtils.getFeatureCalls(propertyCallExpression);

        // since the parameters can only be either dotFeatureCall or
        // arrowFeatureCall we try one or the other.
        String parameters = StringUtils.deleteWhitespace(ConcreteSyntaxUtils.getParametersAsString(
                (AFeatureCall) featureCalls.get(0)));

        String primaryExpression = ConcreteSyntaxUtils.getPrimaryExpression(propertyCallExpression);

        translation = this.replaceFragment(translation, primaryExpression, 1);
        translation = this.replaceFragment(translation, parameters, 0);

        this.getExpression().appendSpaceToTranslatedExpression();
        this.getExpression().appendToTranslatedExpression(translation);
    }

    /**
     * @param translation
     * @param node
     */
    public void handleDotOperation(String translation, Object node)
    {
        APropertyCallExpression propertyCallExpression = (APropertyCallExpression) node;
        String firstArgument = ConcreteSyntaxUtils.getPrimaryExpression(propertyCallExpression);
        translation = this.replaceFragment(translation, firstArgument, 0);
        List featureCalls = ConcreteSyntaxUtils.getFeatureCalls(propertyCallExpression);
        if (featureCalls != null && !featureCalls.isEmpty())
        {
            // here we loop through the feature calls and find the ones
            // that are operation feature calls, we then retrieve and replace
            // all parameters in the translated expression
            for (final Iterator callIterator = featureCalls.iterator(); callIterator.hasNext();)
            {
                AFeatureCall featureCall = (AFeatureCall) callIterator.next();

                if (TranslationUtils.trimToEmpty(featureCall).matches(OCLPatterns.OPERATION_FEATURE_CALL))
                {
                    List parameters = ConcreteSyntaxUtils.getParameters(featureCall);
                    if (parameters != null && !parameters.isEmpty())
                    {
                        Iterator parameterIterator = parameters.iterator();
                        for (int ctr = 1; parameterIterator.hasNext(); ctr++)
                        {
                            translation = this.replaceFragment(translation, (String) parameterIterator.next(), ctr);
                        }
                    }
                    break;
                }
            }
        }
        this.getExpression().appendSpaceToTranslatedExpression();
        this.getExpression().appendToTranslatedExpression(translation);
    }

    private StringBuffer sortedByClause;

    /**
     * @param translation
     * @param node
     */
    public void handleSortedBy(String translation, Object node)
    {
        if (this.sortedByClause.length() > 0)
        {
            this.sortedByClause.append(", ");
        }
        this.sortedByClause.append(TranslationUtils.deleteWhitespace(ConcreteSyntaxUtils.getParametersAsString(
                (AFeatureCall) node)));
    }

    /*------------------------- Logical Expression Handler (and, or, xor, etc.) ----------------------*/

    /**
     * @param translation
     * @param node
     */
    public void handleLogicalExpression(String translation, Object node)
    {
        this.getExpression().appendSpaceToTranslatedExpression();
        this.getExpression().appendToTranslatedExpression(translation);
    }

    /*------------------------- Relational Expression Handler (=, <, >, >=, etc.) --------------------*/

    /**
     * @param translation
     * @param node
     */
    public void handleRelationalExpression(String translation, Object node)
    {
        ARelationalExpressionTail relationalExpressionTail = (ARelationalExpressionTail) node;

        String[] leftAndRightExpressions = ConcreteSyntaxUtils.getLeftAndRightExpressions(
                (PRelationalExpression) relationalExpressionTail.parent());
        String leftExpression = StringUtils.deleteWhitespace(leftAndRightExpressions[0]);
        String rightExpression = StringUtils.deleteWhitespace(leftAndRightExpressions[1]);
        if (leftExpression.matches(OCLPatterns.OPERATION_FEATURE_CALL))
        {
            leftExpression = "";
        }
        translation = this.replaceFragment(translation, leftExpression, 0);
        if (rightExpression.matches(OCLPatterns.OPERATION_FEATURE_CALL))
        {
            rightExpression = "";
        }
        translation = this.replaceFragment(translation, rightExpression, 1);

        this.getExpression().appendSpaceToTranslatedExpression();
        this.getExpression().appendToTranslatedExpression(translation);
    }
}

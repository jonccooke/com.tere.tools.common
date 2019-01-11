package com.tere.mapping.xpath;

import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;

import com.tere.logging.LogManager;
import com.tere.logging.Logger;
import com.tere.utils.directory.FileUtils;

import main.atlr4.XPathLexer;
import main.atlr4.XPathListener;
import main.atlr4.XPathParser;
import main.atlr4.XPathParser.AbbreviatedStepContext;
import main.atlr4.XPathParser.AbsoluteLocationPathNorootContext;
import main.atlr4.XPathParser.AdditiveExprContext;
import main.atlr4.XPathParser.AndExprContext;
import main.atlr4.XPathParser.AxisSpecifierContext;
import main.atlr4.XPathParser.EqualityExprContext;
import main.atlr4.XPathParser.ExprContext;
import main.atlr4.XPathParser.FilterExprContext;
import main.atlr4.XPathParser.FunctionCallContext;
import main.atlr4.XPathParser.FunctionNameContext;
import main.atlr4.XPathParser.LocationPathContext;
import main.atlr4.XPathParser.MainContext;
import main.atlr4.XPathParser.MultiplicativeExprContext;
import main.atlr4.XPathParser.NCNameContext;
import main.atlr4.XPathParser.NameTestContext;
import main.atlr4.XPathParser.NodeTestContext;
import main.atlr4.XPathParser.OrExprContext;
import main.atlr4.XPathParser.PathExprNoRootContext;
import main.atlr4.XPathParser.PredicateContext;
import main.atlr4.XPathParser.PrimaryExprContext;
import main.atlr4.XPathParser.QNameContext;
import main.atlr4.XPathParser.RelationalExprContext;
import main.atlr4.XPathParser.RelativeLocationPathContext;
import main.atlr4.XPathParser.StepContext;
import main.atlr4.XPathParser.UnaryExprNoRootContext;
import main.atlr4.XPathParser.UnionExprNoRootContext;
import main.atlr4.XPathParser.VariableReferenceContext;

public class XPathTest
{
	private static Logger log = LogManager.getLogger(XPathTest.class);
	
	@Test
	public void testXPathParser() throws IOException
	{
		StringBuffer xpathContent = FileUtils.readTextFile("classpath:testdata/xpath/xpath.examples");
		String[] xpaths = xpathContent.toString().split("\n");
		
		XPathLexer lexer= new XPathLexer(CharStreams.fromString(xpaths[0]));
		
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		XPathParser parser = new XPathParser(tokens);
		ParseTree tree = parser.main();
		
		ParseTreeWalker walker = new ParseTreeWalker();
		
		walker.walk(new XPathListener()
		{
			
			@Override
			public void visitTerminal(TerminalNode node)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void visitErrorNode(ErrorNode node)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitEveryRule(ParserRuleContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterEveryRule(ParserRuleContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitVariableReference(VariableReferenceContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitUnionExprNoRoot(UnionExprNoRootContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitUnaryExprNoRoot(UnaryExprNoRootContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitStep(StepContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitRelativeLocationPath(RelativeLocationPathContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitRelationalExpr(RelationalExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitQName(QNameContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitPrimaryExpr(PrimaryExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitPredicate(PredicateContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitPathExprNoRoot(PathExprNoRootContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitOrExpr(OrExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitNodeTest(NodeTestContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitNameTest(NameTestContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitNCName(NCNameContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitMultiplicativeExpr(MultiplicativeExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitMain(MainContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitLocationPath(LocationPathContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitFunctionName(FunctionNameContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitFunctionCall(FunctionCallContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitFilterExpr(FilterExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitExpr(ExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitEqualityExpr(EqualityExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitAxisSpecifier(AxisSpecifierContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitAndExpr(AndExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitAdditiveExpr(AdditiveExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitAbsoluteLocationPathNoroot(AbsoluteLocationPathNorootContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitAbbreviatedStep(AbbreviatedStepContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterVariableReference(VariableReferenceContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterUnionExprNoRoot(UnionExprNoRootContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterUnaryExprNoRoot(UnaryExprNoRootContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterStep(StepContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterRelativeLocationPath(RelativeLocationPathContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterRelationalExpr(RelationalExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterQName(QNameContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterPrimaryExpr(PrimaryExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterPredicate(PredicateContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterPathExprNoRoot(PathExprNoRootContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterOrExpr(OrExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterNodeTest(NodeTestContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterNameTest(NameTestContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterNCName(NCNameContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterMultiplicativeExpr(MultiplicativeExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterMain(MainContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterLocationPath(LocationPathContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterFunctionName(FunctionNameContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterFunctionCall(FunctionCallContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterFilterExpr(FilterExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterExpr(ExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterEqualityExpr(EqualityExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterAxisSpecifier(AxisSpecifierContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterAndExpr(AndExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterAdditiveExpr(AdditiveExprContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterAbsoluteLocationPathNoroot(AbsoluteLocationPathNorootContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void enterAbbreviatedStep(AbbreviatedStepContext ctx)
			{
				// TODO Auto-generated method stub
				
			}
		}, tree);
	}

}

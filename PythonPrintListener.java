import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class MiniCPrintListener extends MiniCBaseListener {

	private ParseTreeProperty<String> nextTexts = new ParseTreeProperty<String>();

	@Override
	public void exitDecl(MiniCParser.DeclContext ctx) {
		nextTexts.put(ctx, nextTexts.get(ctx.getChild(0)));
	}
/////////nextTexts.get(ctx.type_spec()) + " " + 삭제 (type 삭제)
	@Override
	public void exitVar_decl(MiniCParser.Var_declContext ctx) {
		String str;
		if (ctx.getChildCount() == 6 && ctx.getChild(3).getText().equals("["))// array�ϰ��
			str = ctx.IDENT().getText() + ctx.getChild(3).getText()
					+ ctx.LITERAL().getText() + ctx.getChild(5).getText() + ctx.getChild(5).getText() + "\n";
		else if (ctx.getChildCount() == 5 && ctx.getChild(2).getText().equals("="))// =�ϰ��
			str = ctx.IDENT().getText() + " " + ctx.getChild(2).getText() + " "
					+ ctx.LITERAL().getText() + ctx.getChild(4).getText() + "\n";
		else// �ƴҰ��
			str = ctx.IDENT().getText() + ctx.getChild(2).getText() + "\n";

		nextTexts.put(ctx, str);
	}

	@Override
	public void exitProgram(MiniCParser.ProgramContext ctx) {
		String s1, s2 = "";
		int index = 0;
		s1 = nextTexts.get(ctx.decl(index++)) + "\n";
		s2 = s2 + s1;

		while (ctx.getChildCount() > index && ctx.getChild(index) == ctx.decl(index)) { // �� ������� �ݺ�
			s1 = nextTexts.get(ctx.decl(index++)) + "\n";
			s2 = s2 + s1;
		}
		nextTexts.put(ctx, s2);
		System.out.println(s2); // ���������� ����ϱ�

	}

	@Override
	public void exitType_spec(MiniCParser.Type_specContext ctx) {
		String str = "";
//		if (ctx.getChildCount() == 1 && ctx.VOID() != null) {// void
//			str = ctx.VOID().getText();
//		} else {
//			str = ctx.INT().getText();
//		}
		nextTexts.put(ctx, str);
		
	}
	//nextTexts.get(ctx.type_spec()) + " " + 삭제 
	@Override
	public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
		nextTexts.put(ctx, "def " + ctx.IDENT().getText() + "("
				+ nextTexts.get(ctx.params()) + ")\n" + nextTexts.get(ctx.compound_stmt()));
	}

	@Override
	public void exitParams(MiniCParser.ParamsContext ctx) {
		String str = "";
		if (ctx.getChildCount() != 0 && !(ctx.getChildCount() == 1 && ctx.VOID() != null)) {
			System.out.print(ctx.param().get(ctx.param().size()-1));
			System.out.print("\n");
			for (MiniCParser.ParamContext anCtx : ctx.param()) {
				System.out.print(anCtx);
				String tmp = nextTexts.get(anCtx);
				str += tmp; 
				if(anCtx != ctx.param().get(ctx.param().size()-1)) {
					str += ",";
				}
			}
		}
		nextTexts.put(ctx, str);
	}

	//nextTexts.get(ctx.type_spec()) + " " +
	@Override
	public void exitParam(MiniCParser.ParamContext ctx) {
		String p =  ctx.IDENT().getText();
		nextTexts.put(ctx, p);
	}

	@Override
	public void exitStmt(MiniCParser.StmtContext ctx) {
		nextTexts.put(ctx, nextTexts.get(ctx.getChild(0)));
	}

	@Override
	public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
		String str, newStmt = "", space = "    ";
		String[] idStmt;
		String stmt = nextTexts.get(ctx.stmt());
		idStmt = stmt.split("\n");

		if (ctx.stmt().compound_stmt() != null) {
			for (String strI : idStmt)
				newStmt = newStmt + (strI + "\n");
		} else {
			newStmt = newStmt + ("{\n");
			for (String strI : idStmt)
				newStmt = newStmt + (space + strI + "\n");
			newStmt = newStmt + ("}\n");
		}

		nextTexts.put(ctx.stmt(), newStmt);
		str = ctx.getChild(0) + " " + ctx.getChild(1) + nextTexts.get(ctx.expr()) + ctx.getChild(3) + "\n" + newStmt;
		nextTexts.put(ctx, str);
	}

	@Override
	public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		String str = "{\n";
		int NOstmt = ctx.getChildCount();
		for (int i = 1; i < NOstmt - 1; i++) {

			String[] idStmt;
			String stmt = nextTexts.get(ctx.getChild(i));
			String newStmt = "", space = "    ";
			idStmt = stmt.split("\n");

			for (String anIndentedStmt : idStmt)
				newStmt = newStmt + (space + anIndentedStmt + "\n");

			str = str + newStmt;
		}
		str = str + "}\n";
		nextTexts.put(ctx, str);
	}

	// s1 + " " +
	@Override
	public void exitLocal_decl(MiniCParser.Local_declContext ctx) {
		String s1 = null, s2 = null;
		if (ctx.getChildCount() == 3) {
			//s1 = nextTexts.get(ctx.type_spec());
			s2 = ctx.IDENT().getText();
			nextTexts.put(ctx, s2 + ctx.getChild(2) + "\n");
		} else {
			//s1 = nextTexts.get(ctx.type_spec());
			s2 = ctx.IDENT().getText() + ctx.getChild(2) + ctx.LITERAL().getText() + ctx.getChild(4);
					//+ ctx.getChild(5);
			//System.out.println(ctx.getChild(5).getText());
			nextTexts.put(ctx, s2 + "\n");
		}
	}

	@Override
	public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
		String e1, s1;
		e1 = ctx.getChild(0).getText() + " " + ctx.getChild(1).getText() + nextTexts.get(ctx.expr())
				+ ctx.getChild(3).getText() + "\n";
		s1 = nextTexts.get(ctx.stmt(0));

		String[] indentedStmt;
		String newStmt = "";
		String indent = "    ";
		indentedStmt = s1.split("\n");

		if (ctx.stmt(0).compound_stmt() != null) {
			for (String anIndentedStmt : indentedStmt)
				newStmt = newStmt + (anIndentedStmt + "\n");
		} else {
			newStmt = newStmt + "{\n";
			for (String anIndentedStmt : indentedStmt)
				newStmt = newStmt + (indent + anIndentedStmt + "\n");
			newStmt = newStmt + "}\n";
		}
		nextTexts.put(ctx.stmt(0), newStmt);
		nextTexts.put(ctx, e1 + nextTexts.get(ctx.stmt(0)));
	}

	@Override
	public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
		if (ctx.expr() != null)
			nextTexts.put(ctx, ctx.RETURN().getText() + " " + nextTexts.get(ctx.expr()) + ctx.getChild(2) + "\n");
		else
			nextTexts.put(ctx, ctx.RETURN().getText() + ctx.getChild(1) + "\n");
	}

	@Override
	public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		nextTexts.put(ctx, nextTexts.get(ctx.getChild(0)) + ctx.getChild(1).getText() + "\n");
	}

	@Override
	public void exitArgs(MiniCParser.ArgsContext ctx) {
		String str = "";
		for (MiniCParser.ExprContext c : ctx.expr()) {
			str = str + (nextTexts.get(c) + ", ");
		}
		str = str.substring(0, str.length() - 2);
		nextTexts.put(ctx, str);
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
	}

	@Override
	public void exitExpr(MiniCParser.ExprContext ctx) {
		String s1 = null, s2 = null, op = null;
		if (isBinaryOperation(ctx)) {
			s1 = nextTexts.get(ctx.expr(0));
			s2 = nextTexts.get(ctx.expr(1));
			op = ctx.getChild(1).getText();
			nextTexts.put(ctx, s1 + " " + op + " " + s2);
		}

		else if (isID(ctx)) {
			nextTexts.put(ctx, ctx.IDENT().getText());
		}

		else if (isLITERAL(ctx)) {
			nextTexts.put(ctx, ctx.LITERAL().getText());
		}

		else if (isAssign(ctx)) {
			s1 = ctx.IDENT().getText();
			s2 = nextTexts.get(ctx.expr(0));
			op = ctx.getChild(1).getText();
			nextTexts.put(ctx, s1 + " " + op + " " + s2);
		}

		else if (isFunction(ctx)) {
			s1 = ctx.IDENT().getText();
			s2 = nextTexts.get(ctx.args());
			nextTexts.put(ctx, s1 + ctx.getChild(1) + s2 + ctx.getChild(3));
		}

		else if (isFrontOp(ctx)) {
			s1 = nextTexts.get(ctx.expr(0));
			op = ctx.getChild(0).getText();
			nextTexts.put(ctx, op + s1);
		}

		else if (isInBrackets(ctx)) {
			s1 = nextTexts.get(ctx.expr(0));
			nextTexts.put(ctx, ctx.getChild(0).getText() + s1 + ctx.getChild(2));
		}

		else if (isArray(ctx)) {
			s1 = ctx.IDENT().getText();
			s2 = nextTexts.get(ctx.expr(0));
			nextTexts.put(ctx, s1 + " " + ctx.getChild(1) + s2 + ctx.getChild(3));
		}

		else if (isArrayNExpr(ctx)) {
			s1 = ctx.IDENT().getText() + ctx.getChild(1) + nextTexts.get(ctx.expr(0)) + ctx.getChild(3);
			s2 = nextTexts.get(ctx.expr(1));
			nextTexts.put(ctx, s1 + " " + ctx.getChild(4) + " " + s2);
		}

	}

	private boolean isBinaryOperation(MiniCParser.ExprContext ctx) {
		return (ctx.getChildCount() == 3) && (ctx.getChild(1) != ctx.expr()) && (ctx.expr(0) != null)
				&& (ctx.expr(1) != null);
		// ������ �Ұ�� i=2<<�̷��� �ȵ�
	}

	private boolean isArray(MiniCParser.ExprContext ctx) {
		return (ctx.getChildCount() == 4) && (ctx.getChild(1).getText().equals("["));
	}

	private boolean isID(MiniCParser.ExprContext ctx) {
		return (ctx.getChildCount() == 1) && (ctx.IDENT() != null);
	}

	private boolean isAssign(MiniCParser.ExprContext ctx) {
		return (ctx.getChildCount() == 3) && (ctx.IDENT() != null) && (ctx.expr(0) != null);
	}

	private boolean isFunction(MiniCParser.ExprContext ctx) {
		return (ctx.getChildCount() == 4) && (ctx.getChild(1).getText().equals("("));
	}

	private boolean isLITERAL(MiniCParser.ExprContext ctx) {
		return (ctx.getChildCount() == 1) && (ctx.LITERAL() != null);
	}

	private boolean isFrontOp(MiniCParser.ExprContext ctx) {
		return (ctx.getChildCount() == 2) && (ctx.getChild(1) != ctx.expr());
	}

	private boolean isInBrackets(MiniCParser.ExprContext ctx) {
		return (ctx.getChildCount() == 3) && (ctx.getChild(1) == ctx.expr(0));
	}

	private boolean isArrayNExpr(MiniCParser.ExprContext ctx) {
		return (ctx.getChildCount() == 6) && ctx.getChild(1).getText().equals("[");
	}
}
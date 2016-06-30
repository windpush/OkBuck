package com.github.okbuilds.okbuck.rule

final class GenSqlDelightRule extends BuckRule {

    private final String mSqlDelightRunnerPath
    private final String mProjectDir

    GenSqlDelightRule(String name, String sqlDelightRunnerPath, String projectDir) {
        super("gen", name)
        mSqlDelightRunnerPath = sqlDelightRunnerPath
        mProjectDir = projectDir
    }

    @Override
    final void print(PrintStream printer) {
        printer.println("genrule(")
        printer.println("\tname = '${name}',")
        printer.println("\tsrcs = glob([")
        printer.println("\t\t'src/main/sqldelight/**/*.sq',")
        printer.println("\t]),")
        printer.println("\tout = 'out',")
        printer.println("\tbash = 'java -jar ${mSqlDelightRunnerPath} \$SRCDIR ${mProjectDir} && echo \$SRCS > \$OUT',")
        printer.println(")")
        printer.println()
    }

    @Override
    protected void printContent(PrintStream printer) {
    }
}

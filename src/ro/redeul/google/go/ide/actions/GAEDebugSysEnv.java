package ro.redeul.google.go.ide.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import ro.redeul.google.go.config.sdk.GoAppEngineSdkData;
import ro.redeul.google.go.ide.ui.GoToolWindow;
import ro.redeul.google.go.sdk.GoSdkUtil;

public class GAEDebugSysEnv extends GoCommonDebugAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        final Project project = anActionEvent.getData(LangDataKeys.PROJECT);

        if (project == null) {
            return;
        }

        Sdk sdk = GoSdkUtil.getGoogleGAESdkForProject(project);
        if ( sdk == null ) {
            return;
        }

        final GoAppEngineSdkData sdkData = (GoAppEngineSdkData)sdk.getSdkAdditionalData();
        if ( sdkData == null ) {
            return;
        }

        String projectDir = project.getBasePath();
        if (projectDir == null) {
            return;
        }

        try {
            GoToolWindow toolWindow = this.getGoToolWindow(project);
            toolWindow.showAndCreate(project);
            toolWindow.clearConsoleView();

            String[] sysEnv = GoSdkUtil.convertEnvMapToArray(System.getenv());


            toolWindow.printNormalMessage(String.format("%s -> %s%n", "Project dir", projectDir));
            for (String env : sysEnv) {
                toolWindow.printNormalMessage(String.format("%s%n", env));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog("Error while processing go env command.", "Error on go env");
        }
    }
}

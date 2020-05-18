#!/usr/bin/env groovy

def call(stageName, pipelineName, buildNumber) {
    def build = currentBuild.build()
    def action = build.getActions(hudson.tasks.junit.TestResultAction.class)
    if (action) {
        def result = build.getAction(hudson.tasks.junit.TestResultAction.class).getResult();
        if (result) {
            def jsonString = "{'stageName':$stageName, 'name':${result.getDisplayName()}, 'url':${result.getUrl()}, 'totalTests':${totalTests}, 'passedTests':${result.getPassCount()}, 'failedTests':${result.getFailCount()}, 'skippedTests':${result.getSkipCount()}, 'duration':${result.getDuration()}, 'buildNumber':$buildNumber, 'pipelineName':$pipelineName}"
            println jsonString
            def parser = new groovy.json.JsonSlurper()
            def json = parser.parseText(str)
            def response = ["curl", "-k", "-X", "POST", "-H", "Content-Type: application/json", "-d", "${json}", "https://devops.integration.user:devops@192.168.0.110:8080/api/sn_devops/v1/devops/tool/test?toolId=bb7526d55b3c1010598a16a0ab81c755&testType=Integration"].execute()
            response.waitFor()
            println response.err.text
            println response.text
        }
    }
}
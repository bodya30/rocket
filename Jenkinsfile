#!/usr/bin/env groovy

def cmd_exec(command)
{
    return bat(returnStdout: true, script: "${command}").trim()
}

node
{
    stage('checkout master')
    {
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false,
        extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/bodya30/rocket.git']]])
    }
    stage('build')
    {
        cmd_exec('gradlew.bat clean build -x test')
    }
    stage('unit tests')
    {
        cmd_exec('gradlew.bat test')
    }
    stage('integration tests')
    {
        cmd_exec('gradlew.bat integrationTest')
    }
}

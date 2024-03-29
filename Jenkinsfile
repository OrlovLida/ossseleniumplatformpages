properties([
        [$class: 'GitLabConnectionProperty', gitLabConnection: 'GitlabMakumba'],
        disableConcurrentBuilds()
])
String cron_string = BRANCH_NAME == "master" ? "5 22 * * *" : ""

def sendEmail(){
    script {
        if(BRANCH_NAME == "master"){
            def emailReportRecipients = 'Krzysztof.Pogonowski@comarch.pl,'
            step([
                $class                  : 'Mailer',
                notifyEveryUnstableBuild: true,
                recipients              : emailReportRecipients + emailextrecipients([
                        [$class: 'CulpritsRecipientProvider'],
                        [$class: 'RequesterRecipientProvider']
                ]),
                sendToIndividuals       : true
            ])
        }
    }
}

pipeline {
    agent any
    triggers {
        gitlab(triggerOnPush: true)
        cron(cron_string)
    }
    options {
        skipDefaultCheckout(true)
    }

    stages {

        stage('Checkout Git') {
            steps {
                deleteDir()
                checkout scm
            }
        }

        stage('Compile sources') {
            steps {
                script {
                    if (BRANCH_NAME == "master") {
                        configFileProvider([configFile(fileId: '4bcc5ae5-ad71-4c46-a4d7-7f48e45e341c', variable: 'MAVEN_SETTINGS')]) {
                            sh 'mvn -T 6 clean install -DskipTests -U -f $WORKSPACE/pom.xml -s $MAVEN_SETTINGS -P legacy'
                        }
                    } else {
                        configFileProvider([configFile(fileId: '4bcc5ae5-ad71-4c46-a4d7-7f48e45e341c', variable: 'MAVEN_SETTINGS')]) {
                            sh 'mvn -T 6 clean compile -DskipTests -U -f $WORKSPACE/pom.xml -s $MAVEN_SETTINGS -P legacy'
                        }
                    }
                }
            }
        }

        stage('Nexus Deploy') {
            when {
                branch 'master'
            }
            steps {
                configFileProvider([configFile(fileId: '4bcc5ae5-ad71-4c46-a4d7-7f48e45e341c', variable: 'MAVEN_SETTINGS')]) {
                    sh "mvn deploy -DskipTests -f $WORKSPACE/pom.xml -s \$MAVEN_SETTINGS"
                }
            }
        }
    }

    post {
        failure {
            sendEmail()
        }
        unstable {
            sendEmail()
        }
        fixed {
            sendEmail()
        }
    }
}

# [Loose touch](http://www.loosetouch.com)
_Loose touch reminds you to keep in touch with your key contacts_

# Project organization.

## Requirements.
You need to install the following components :
 * [Git](https://git-scm.com/) : `sudo apt-get -y install git`
 * [Java8](https://openjdk.java.net/install/) : `sudo apt-get -y install openjdk-8-jdk`
 * [Maven](https://maven.apache.org/) : `sudo apt-get -y install maven`
 * [Node.js](https://nodejs.org/) : `sudo apt -y install nodejs`
 * [Npm](https://www.npmjs.com/) : `sudo apt -y install npm`
 * [AWS Sam local](https://github.com/awslabs/aws-sam-cli) : `sudo npm install -g aws-sam-local`

## Sources.
The [project](https://github.com/straumat/loose-touch) is composed of the following sub projects :
 * loose-touch-server : back end.
 * loose-touch-chrome-extension : contains the chrome extension.
 * loose-touch-web-site : contains the public web site deployed to S3.

## Project release guide.
In a shell :
 * Type `git remote -v` and check that you are in ssh and not https.
 * If you are in https, type : `git remote set-url origin git@github.com:straumat/loose-touch.git`.
 * Start the release with the command : `mvn gitflow:release-start`.
 * Finish the release with the command : `mvn gitflow:release-finish`.
 * If the plugin did not push everything, run : `git push --tags;git push origin master;git push origin development`.
 * Go back to the development branch with the command : `git checkout development`.

## Components.

### Application infrastructure.
 * [AWS CloudFormation](https://aws.amazon.com/en/cloudformation/).
 * [AWS Lambda](https://aws.amazon.com/en/lambda/).
 * [Amazon DynamoDB](https://aws.amazon.com/en/dynamodb/). 

### Development infrastructure.
 * [Github](https://github.com/straumat/loose-touch).
 * [Dependabot](https://dependabot.com/).

### Frameworks.
 * [Maven](https://maven.apache.org/).
 * [Spring boot](https://spring.io/projects/spring-boot).
 * [AWS Serverless Java Container](https://github.com/awslabs/aws-serverless-java-container).

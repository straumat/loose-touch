# Developer installation guide.
 * [Git](https://git-scm.com/) : `sudo apt-get -y install git`
 * [Java8](https://openjdk.java.net/install/) : `sudo apt-get -y install openjdk-8-jdk`
 * [Maven](https://maven.apache.org/) : `sudo apt-get -y install maven`
 * [jq](https://stedolan.github.io/jq/) : `sudo apt-get -y install jq`
 * [AWS CLI](https://aws.amazon.com/cli/) : `sudo apt install awscli`
 * [Node.js](https://nodejs.org/) : `sudo apt -y install nodejs`
 * [Npm](https://www.npmjs.com/) : `sudo apt -y install npm`
 * [AWS Sam local](https://github.com/awslabs/aws-sam-cli) : `sudo npm install -g aws-sam-local`
 
Some things to finish the configuration : 
 * `echo 'export PATH=~/.npm-global/bin:$PATH' >> ~/.profile` (restart your shell after)
 * Configure your aws credentials with the command `aws configure`.
 
_note : if you have permissions issues with aws-sam-local, try this : `npm config set unsafe-perm=true`._
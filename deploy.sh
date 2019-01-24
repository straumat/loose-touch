#!/usr/bin/env bash
set -e

# ======================================================================================================================
# Project configuration.
ENVIRONMENT=$1
REGION="eu-west-3"
BUCKET="loose-touch-configuration"
SERVICE="loose-touch"
SAM_FILENAME="aws-configuration.yml"
# ======================================================================================================================

# ======================================================================================================================
# Environment parameter is required.
if [ -z "$1" ]
    then
    echo "[ERROR] Argument 'environment' is required."
    exit 1
fi
# ======================================================================================================================

# ======================================================================================================================
# Environment parameter is not valid.
if [[ "$1" != "test" && "$1" != "stage" && "$1" != "production" ]]
    then
    echo "[ERROR] Argument '$1' is not valid. It must be either test, stage or production."
    exit 1
fi
# ======================================================================================================================

# ======================================================================================================================
# Build the back-end.
echo "==="
echo "Building the back end ..."
mvn -f back-end/pom.xml clean package
# ======================================================================================================================

# ======================================================================================================================
# CloudFormation packaging...
echo "==="
echo "CloudFormation packaging for $1 ..."
aws cloudformation package \
    --region ${REGION} \
    --s3-bucket ${BUCKET} \
    --template-file ${SAM_FILENAME} \
    --output-template-file output-$1-$SAM_FILENAME
# ======================================================================================================================

# ======================================================================================================================
# CloudFormation deploying...
echo "==="
echo "CloudFormation deploying $1 ..."
aws cloudformation deploy  \
     --region ${REGION} \
     --template-file output-$1-$SAM_FILENAME \
     --stack-name ${SERVICE}-$1 \
     --capabilities CAPABILITY_NAMED_IAM \
     --parameter-override Stage=$1
# ======================================================================================================================

# ======================================================================================================================
echo "==="
echo "Details of stack $1 ..."
aws cloudformation describe-stacks --stack-name loose-touch-$1 --region eu-west-3
# ======================================================================================================================

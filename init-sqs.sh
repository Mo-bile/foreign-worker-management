CONTAINER="foreign-worker-compliance-localstack-1"
REGION="ap-northeast-2"

# 1. DLQ 생성
docker exec $CONTAINER awslocal sqs create-queue \
  --queue-name compliance-alert-dlq \
  --region $REGION

# 2. DLQ ARN 조회
  DLQ_ARN=$(docker exec $CONTAINER awslocal sqs get-queue-attributes \
    --queue-url http://localhost:4566/000000000000/compliance-alert-dlq \
    --attribute-names QueueArn \
    --region $REGION \
    --query 'Attributes.QueueArn' \
    --output text)

# 3. Main Queue 생성 (DLQ 연결)
docker exec $CONTAINER awslocal sqs create-queue \
  --queue-name compliance-alert-queue \
  --region $REGION \
  --attributes '{
    "RedrivePolicy":
"{\"deadLetterTargetArn\":\"'"$DLQ_ARN"'\",\"maxReceiveCount\":\"3\"}",
    "VisibilityTimeout": "30",
    "ReceiveMessageWaitTimeSeconds": "20"
  }'

echo "=== Queues Created ==="
docker exec $CONTAINER awslocal sqs list-queues --region $REGION
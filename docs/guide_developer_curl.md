# Using curl.

## Set server url.

### Local.
```
export LOOSE_TOUCH_SERVER_URL="http://127.0.0.1:8080"
```
### AWS - staging.
```
export LOOSE_TOUCH_SERVER_URL="https://api.dumbtest.pw/staging"
```

## Define google id token for user 1. 
```
export GOOGLE_ID_TOKEN_FOR_USER_1=`curl https://www.googleapis.com/oauth2/v4/token -d client_id=408314219149-60s8l2ltrbal82hnuj35u81opt27ghsa.apps.googleusercontent.com -d client_secret=NsR7_eU8KCLZ85BEzske6v_C -d refresh_token=1/ST2ikxLlVoH3K352DN60nUQn8KW4NBpf2er2Q7QReF7qxNrXGNr4jqeLRBlPdwyc -s -d grant_type=refresh_token | jq -r '.id_token'`
```

## Define google access token for user 1. 
```
export GOOGLE_ACCESS_TOKEN_FOR_USER_1=`curl https://www.googleapis.com/oauth2/v4/token -d client_id=408314219149-60s8l2ltrbal82hnuj35u81opt27ghsa.apps.googleusercontent.com -d client_secret=NsR7_eU8KCLZ85BEzske6v_C -d refresh_token=1/ST2ikxLlVoH3K352DN60nUQn8KW4NBpf2er2Q7QReF7qxNrXGNr4jqeLRBlPdwyc -s -d grant_type=refresh_token | jq -r '.access_token'`
```

## Define loose touch token for user 1.
```
export LOOSE_TOUCH_ID_TOKEN_FOR_USER_1=` curl "${LOOSE_TOUCH_SERVER_URL}/v1/login/google?googleIdToken=${GOOGLE_ID_TOKEN_FOR_USER_1}&googleAccessToken=${GOOGLE_ACCESS_TOKEN_FOR_USER_1}" -X GET -s | jq -r '.idToken'`
```

## Get profile information.
```
curl "${LOOSE_TOUCH_SERVER_URL}/v1/user/profile" -H "Authorization: Bearer ${LOOSE_TOUCH_ID_TOKEN_FOR_USER_1}" -X GET -s | jq
```

## Add 3 contacts.

### User 1 / Contact 1.
```
curl    --header "Content-Type: application/json" \
        --header "Authorization: Bearer ${LOOSE_TOUCH_ID_TOKEN_FOR_USER_1}" \
        --request POST \
        --data '{"email":"test1@test.com","firstName":"first name test 1","lastName":"last name test 1","notes":"notes 1","contactRecurrenceType":"DAY","contactRecurrenceValue":1,"contactDueDate":"31/12/2019"}' \
        ${LOOSE_TOUCH_SERVER_URL}/v1/contacts | jq
```

### User 1 / Contact 2.
```
curl    --header "Content-Type: application/json" \
        --header "Authorization: Bearer ${LOOSE_TOUCH_ID_TOKEN_FOR_USER_1}" \
        --request POST \
        --data '{"email":"test2@test.com","firstName":"first name test 2","lastName":"last name test 2","notes":"notes 2","contactRecurrenceType":"MONTH","contactRecurrenceValue":2,"contactDueDate":"16/11/2019"}' \
        ${LOOSE_TOUCH_SERVER_URL}/v1/contacts | jq
```

### User 1 / Contact 3.
```
curl    --header "Content-Type: application/json" \
        --header "Authorization: Bearer ${LOOSE_TOUCH_ID_TOKEN_FOR_USER_1}" \
        --request POST \
        --data '{"email":"test3@test.com","firstName":"first name test 3","lastName":"last name test 3","notes":"notes 3","contactRecurrenceType":"YEAR","contactRecurrenceValue":3,"contactDueDate":"01/09/2017"}' \
        ${LOOSE_TOUCH_SERVER_URL}/v1/contacts | jq
```

## Get contact to reach.
```
curl "${LOOSE_TOUCH_SERVER_URL}/v1/contacts/toReach" -H "Authorization: Bearer ${LOOSE_TOUCH_ID_TOKEN_FOR_USER_1}" -X GET -s | jq
```

## Get a contact.
```
curl "${LOOSE_TOUCH_SERVER_URL}/v1/contacts/test1@test.com/" -H "Authorization: Bearer ${LOOSE_TOUCH_ID_TOKEN_FOR_USER_1}" -X GET -s | jq
```
# Using curl.

## Set server url.

### Local.
```
export LOOSE_TOUCH_SERVER_URL="http://127.0.0.1:8080"
```
### AWS.
```
export LOOSE_TOUCH_SERVER_URL="https://nb12h4yejg.execute-api.eu-west-3.amazonaws.com/test"
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
curl "${LOOSE_TOUCH_SERVER_URL}/v1/profile" -H "Authorization: Bearer ${LOOSE_TOUCH_ID_TOKEN_FOR_USER_1}" -X GET -s | jq
```
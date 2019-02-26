# Using curl.

## Retrieve google token as environment variable.
### User 1. 
```
export GOOGLE_TOKEN_FOR_USER_1=`curl https://www.googleapis.com/oauth2/v4/token -d client_id=408314219149-60s8l2ltrbal82hnuj35u81opt27ghsa.apps.googleusercontent.com -d client_secret=NsR7_eU8KCLZ85BEzske6v_C -d refresh_token=1/IMZ1k7G6ksE71CrvuaLXyKXswIIseVo039wv1cSwzY4 -s -d grant_type=refresh_token | jq -r '.id_token'`
```
###
```
export GOOGLE_TOKEN_FOR_USER_2=`curl https://www.googleapis.com/oauth2/v4/token -d client_id=408314219149-60s8l2ltrbal82hnuj35u81opt27ghsa.apps.googleusercontent.com -d client_secret=NsR7_eU8KCLZ85BEzske6v_C -d refresh_token=1/lgRDorbgTjsJJvAJoyOjlLbVXBDiIiq2WK0mw9a_tNI -s -d grant_type=refresh_token | jq -r '.id_token'`
```

## Retrieve loose touch token as environment variable.

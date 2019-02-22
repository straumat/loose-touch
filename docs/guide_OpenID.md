# OpenID guide.

## Google.

### How it works.
Google's OAuth 2.0 APIs (conform to the OpenID Connect specification, and is OpenID Certified) can be used for both authentication and authorization. 

Before your application can use Google's OAuth 2.0 authentication system for user login, you must set up a project in the [Google API Console](https://console.developers.google.com/) to obtain OAuth 2.0 credentials, set a redirect URI, and (optionally) customize the branding information that your users see on the user-consent screen

We use two ways to interact : 

 * [Google Sign-In](https://developers.google.com/identity/sign-in/) :  allowing to easily connect from our homepage.
 * [Google client libraries](https://developers.google.com/identity/protocols/OpenIDConnect#libraries) : allowing our back end communicate to Google servers to verify and get information from google token.

### Tests accounts.
  * Loose touch
    * Client ID : 408314219149-60s8l2ltrbal82hnuj35u81opt27ghsa.apps.googleusercontent.com
    * Client Secret : NsR7_eU8KCLZ85BEzske6v_C

  * Account loose.touch.test.1@gmail.com
    * First name : loose 1
    * Last name : touch 1
    * Password : stpide123 
    * Refresh token : 1/IMZ1k7G6ksE71CrvuaLXyKXswIIseVo039wv1cSwzY4

  * Account loose.touch.test.2@gmail.com
    * First name : loose 2
    * Last name : touch 2
    * Password : stpide123 
    * Refresh token : 1/2WbPyTeIpT2CH7744KE77WgcCWBxPjZkGRKYM2EUsWsMScMFzyZq0GmGy2WONcBp

### How we use it.
We use [Google Sign-In](https://developers.google.com/identity/sign-in/) to display a google sign-in button that launches the permission page and returns the google id_token to the page. The page then calls our back that check the google token and retrieves user information thanks to [Google client libraries](https://developers.google.com/identity/protocols/OpenIDConnect#libraries). Our back end returns our own JWT token.

This is the code that allows us to retrieve, from our web page, the google Id Token
```
<html lang="en">
  <head>
    <meta name="google-signin-scope" content="profile email">
    <meta name="google-signin-client_id" content="332245545817-5nl0021fkonhclc7rbj5ehm3j2h51u8o.apps.googleusercontent.com">
    <script src="https://apis.google.com/js/platform.js" async defer></script>
  </head>
  <body>
    <div class="g-signin2" data-onsuccess="onSignIn" data-theme="dark"></div>
    <script>
      function onSignIn(googleUser) {
        var profile = googleUser.getBasicProfile();
        // The ID token you need to pass to your backend:
        var id_token = googleUser.getAuthResponse().id_token;
        console.log("ID Token: " + id_token);
      }
    </script>
  </body>
</html>
```

Then, we send this token to the back end where the token will be verified :
```
    public final Optional<Payload> verifyToken(final String idToken) {
        log.debug("Verifying token " + idToken);

        // Creates the google token verifier.
        final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.
                Builder(TRANSPORT, JSON_FACTORY)
                .setIssuers(Arrays.asList("https://accounts.google.com", "accounts.google.com"))
                .setAudience(Collections.singletonList(clientId))
                .build();

        // Token verification.
        GoogleIdToken googleIdToken = null;
        try {
            googleIdToken = verifier.verify(idToken);
        } catch (IllegalArgumentException | GeneralSecurityException | IOException e) {
            log.debug("Error during token verification : " + e.getMessage());
            return Optional.empty();
        }

        // If token is null, the token is invalid.
        if (googleIdToken == null) {
            log.debug("Error during token verification googleIdToken is null");
            return Optional.empty();
        } else {
            // We return the parsed token information.
            log.debug("Token " + idToken + " is valid");
            return Optional.of(googleIdToken.getPayload());
        }
    }
```


, user information will be retrieved and a loose-touch token will be returned.

### How to get the refresh token.
A google token expires after one hour. To get a new valid token, we must get from Google a refresh token for the application. 

To do this, go to [Google ads](https://ads.google.com), go tools/script and creates this script :

```
/**
 * This script allows the stepping through of the Authorization Code Grant in
 * order to obtain a refresh token.
 *
 * This script uses the out-of-band redirect URI, which is not part of the
 * OAuth2 standard, to allow not redirecting the user. If this does not work
 * with your API, try instead the OAuth playground:
 * https://developers.google.com/oauthplayground/
 *
 * Execute script twice:
 * Execution 1: will result in a URL, which when placed in the browser will
 * issue a code.
 * Execution 2: place the code in "CODE" below and execute. If successful a
 * refresh token will be printed to the console.
 */
var CLIENT_ID = '408314219149-60s8l2ltrbal82hnuj35u81opt27ghsa.apps.googleusercontent.com';
var CLIENT_SECRET = 'NsR7_eU8KCLZ85BEzske6v_C';

// Enter required scopes, e.g. ['https://www.googleapis.com/auth/drive']
var SCOPES = ['email profile'];

// Auth URL, e.g. https://accounts.google.com/o/oauth2/auth
var AUTH_URL = 'https://accounts.google.com/o/oauth2/auth';
// Token URL, e.g. https://accounts.google.com/o/oauth2/token
var TOKEN_URL = 'https://accounts.google.com/o/oauth2/token';

// After execution 1, enter the OAuth code inside the quotes below:
var CODE = '';

function main() {
  if (CODE) {
    generateRefreshToken();
  } else {
    generateAuthUrl();
  }
}

/**
 * Creates the URL for pasting in the browser, which will generate the code
 * to be placed in the CODE variable.
 */
function generateAuthUrl() {
  var payload = {
    scope: SCOPES.join(' '),
    // Specify that no redirection should take place
    // This is Google-specific and not part of the OAuth2 specification.
    redirect_uri: 'http://localhost:8080',
    response_type: 'code',
    access_type: 'offline',
    prompt: 'consent',
    client_id: CLIENT_ID
  };
  var options = {payload: payload};
  var request = UrlFetchApp.getRequest(AUTH_URL, options);
  Logger.log(
      'Browse to the following URL: ' + AUTH_URL + '?' + request.payload);
}

/**
 * Generates a refresh token given the authorization code.
 */
function generateRefreshToken() {
  var payload = {
    code: CODE,
    client_id: CLIENT_ID,
    client_secret: CLIENT_SECRET,
    // Specify that no redirection should take place
    // This is Google-specific and not part of the OAuth2 specification.
    redirect_uri: 'http://localhost:8080',
    grant_type: 'authorization_code',
    approval_prompt: 'force'
  };
  var options = {method: 'POST', payload: payload};
  var response = UrlFetchApp.fetch(TOKEN_URL, options);
  var data = JSON.parse(response.getContentText());
  if (data.refresh_token) {
    var msg = 'Success! Refresh token: ' + data.refresh_token +
      '\n\nThe following may also be a useful format for pasting into your ' +
      'script:\n\n' +
      'var CLIENT_ID = \'' + CLIENT_ID + '\';\n' +
      'var CLIENT_SECRET = \'' + CLIENT_SECRET + '\';\n' +
      'var REFRESH_TOKEN = \'' + data.refresh_token + '\';';
    Logger.log(msg);
  } else {
    Logger.log(
        'Error, failed to generate Refresh token: ' +
        response.getContentText());
  }
}
```

Run it once, it will provides you an url you have to connect to, in the URL, you will a code looking like `4/8QAS9khcUa0SuJ35AP_x6pVNp3lrS5DPgbYK5AN6Z42Yt4tidS7KP-NwRCIekkn7qAllFhAlv-gwnNZzT5fGOX4`.

Insert this code in script as the value of the CODE variable and run it again, it will display your refresh code.

You can now get as many refresh token as you want with the command :
  * loose.touch.test.1@gmail.com : `curl https://www.googleapis.com/oauth2/v4/token -d client_id=408314219149-60s8l2ltrbal82hnuj35u81opt27ghsa.apps.googleusercontent.com -d client_secret=NsR7_eU8KCLZ85BEzske6v_C -d refresh_token=1/IMZ1k7G6ksE71CrvuaLXyKXswIIseVo039wv1cSwzY4 -d grant_type=refresh_token`
  * loose.touch.test.2@gmail.com : `curl https://www.googleapis.com/oauth2/v4/token -d client_id=408314219149-60s8l2ltrbal82hnuj35u81opt27ghsa.apps.googleusercontent.com -d client_secret=NsR7_eU8KCLZ85BEzske6v_C -d refresh_token=1/2WbPyTeIpT2CH7744KE77WgcCWBxPjZkGRKYM2EUsWsMScMFzyZq0GmGy2WONcBp -d grant_type=refresh_token`

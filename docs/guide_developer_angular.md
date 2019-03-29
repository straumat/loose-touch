# Guide to angular.

## Tools.
  * [Node.js](https://nodejs.org) : Node.js is an open source server environment that uses JavaScript on the server.
  * [Npm](https://www.npmjs.com) : Npm is the a software registry used to share and borrow packages.
  * [Yarn](https://yarnpkg.com) : Like Npm, Fast, Yarn is a dependency management.
  * [Protractor](https://www.protractortest.org) : Protractor is an end-to-end test framework for Angular and AngularJS applications. Protractor runs tests against your application running in a real browser, interacting with it as a user would.
  
_note : Yarn manipulates package.json files exactly like npm. You can use both._

## Project files.
  * e2e : Directory containing end to end tests made with Protractor. 
  * node_modules : The node_modules is only for build tools. The package.json file in the app root defines what libraries will be installed into node_modules when you run npm install.
  * src : The src/ subfolder contains the source files (app logic, data, and assets), along with configuration files for the initial app.
  * .editorconfig : The EditorConfig project consists of a file format for defining coding styles and a collection of text editor plugins that enable editors to read the file format and adhere to defined styles.
  * angular.json : CLI configuration defaults for all projects in the workspace, including configuration options for build, serve, and test tools that the CLI uses, such as TSLint, Karma, and Protractor.
  * package.json : Configures npm package dependencies that are available to all projects in the workspace.
  * tsconfig.json : Default TypeScript configuration for apps in the workspace, including TypeScript and Angular template compiler options
  * tslint.json : Default TSLint configuration for apps in the workspace.
  * yarn.lock : In order to get consistent installs across machines, Yarn needs more information than the dependencies you configure in your package.json. Yarn needs to store exactly which versions of each dependency were installed.

## Sources files.
  * app : Contains the component files in which your app logic and data are defined.
  * assets : Contains image files and other asset files to be copied as-is when you build your application.
  * environments : 	Contains build configuration options for particular target environments. By default there is an unnamed standard development environment and a production ("prod") environment.
  * browserslist : Configures sharing of target browsers and Node.js versions among various front-end tools.
  * index.html : The main entry point for your app. Compiles the application with the JIT compiler and bootstraps the application's root module (AppModule) to run in the browser.
  * polyfills.ts : Provides polyfill scripts for browser support. A polyfill, or polyfiller, is a piece of code (or plugin) that provides the technology that you, the developer, expect the browser to provide natively. 
  * styles.sass : Lists CSS files that supply styles for a project.
  * test.ts : The main entry point for your unit tests, with some Angular-specific configuration. You don't typically need to edit this file.
  * tsconfig.app.json : Inherits from the workspace-wide tsconfig.json file.
  * tsconfig.spec.json : Inherits from the workspace-wide tsconfig.json file.
  * tslint.json	: Inherits from the workspace-wide tslint.json file.

## App files.
  * app/app.component.ts : Defines the logic for the app's root component, named AppComponent.
  * app/app.component.html : Defines the HTML template associated with the root AppComponent.
  * app/app.component.css : Defines the base CSS stylesheet for the root AppComponent.
  * app/app.component.spec.ts : Defines a unit test for the root AppComponent.
  * app/app.module.ts : Defines the root module, named AppModule, that tells Angular how to assemble the application. Initially declares only the AppComponent. As you add more components to the app, they must be declared here.
  
## Project structure.
A good guideline to follow is to split our application into at least three different modules — Core, Shared and Feature :
  * app/shared - This is the module where I keep small stuff that every other module will need.
  * app/core - Services that app needs (and cannot work without) go here. Examples: ui.service,  auth.service, auth.guard, data.service, workers.service...
  * app/features - This is the module where app functionalities are. They are organized in several submodules. If you app plays music, this is where player, playlist, favorites submodules would go.

links
  * [Application Structure Using Modules](https://www.intertech.com/Blog/angular-module-tutorial-application-structure-using-modules/)
  * [Angular Router Tutorial: Setting Up Routing in Your Application](https://www.intertech.com/Blog/angular-router-tutorial-setting-up-routing-in-your-application/)
  * [6 Best Practices & Pro Tips when using Angular CLI](https://medium.com/@tomastrajan/6-best-practices-pro-tips-for-angular-cli-better-developer-experience-7b328bc9db81)
  
# Notes
  * Added ng-bootstrap
  * Added https://material.angular.io/ 
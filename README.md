# word penne

## Setup

Please make sure the following works.

* [direnv](https://direnv.net)
* Node.js
* Clojure

Place the `.envrc` that we will provide separately in the root directory of the repository.

Install the library by running the following command.

````
npm install
```

## Dev

First, start the firebase emulators for development by running the following command: ``` npm install ``` ## Dev

```
npm run emulators
```

In another terminal, run the following.

````
npm run dev
````

The system will run on localhost:8080.

## Test

First, start the firebase emulators for testing by running the following command: ```` npm run dev ``` localhost:8080 ``` ## Test

```
npm run emulators:test
````

We will run E2E tests with Cypress, so start the server in a separate terminal.

````
npm run dev
```

Again, in another terminal (i.e., the third one), run the following commands to test with Cypress.

```
npm run cypress:open
# or
npm run cypress:run
```

## Branch Rules

* Follow [git-flow](http://danielkummer.github.io/git-flow-cheatsheet/)
* Use https://github.com/conventional-changelog/standard-version to manage versions

## Commit Rules

* Follow [Conventinal Commits](https://www.conventionalcommits.org/ja/v1.0.0/)
* For VS Code, use [vscode-conventional-commits](https://marketplace.visualstudio.com/items?itemName=vivaxy.vscode-conventional-commits) It is convenient to use [vscode-conventional-commits]()

## Deploy

### Verify that the code works in the production environment

Please use the following commands to verify the operation before production deployment.

````
npm run release
npm run emulators:hosting
```

### standard-version to version up and deploy to firebase

``` make deploy
make deploy
make deploy ```

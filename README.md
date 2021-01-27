# word penne

## Setup

次が動くようにしておいてください。

* [direnv](https://direnv.net)
* Node.js
* Clojure

別途お渡しする `.envrc` をリポジトリのルートディレクトリに配置してください。

次のコマンドを実行してライブラリをインストールしてください。

```
npm install
```

## Dev

まず次のコマンドを実行して開発用の firebase emulators を起動してください。

```
npm run emulators
```

別のターミナルで次を実行してください。

```
npm run dev
```

localhost:8080 でシステムが動作します。

## Test

まず次のコマンドを実行してテスト用の firebase emulators を起動してください。

```
npm run emulators:test
```

次のコマンドで Cypress によるテストを実行できます。

```
npm run cypress:open
# または
npm run cypress:run
```

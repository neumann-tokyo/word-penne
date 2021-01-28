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

Cypress による E2E テストを行うので別のターミナルでサーバーを起動します。

```
npm run dev
```

再び別のターミナル（つまり３つ目）で次のコマンドを実行して Cypress によるテストを行います。

```
npm run cypress:open
# または
npm run cypress:run
```

## Branch Rules

* WIP [git-flow](http://danielkummer.github.io/git-flow-cheatsheet/) を使うか検討中
* WIP https://github.com/conventional-changelog/standard-version を使いたい

現状のブランチ構成

* production ブランチ ... 本番デプロイ用のブランチ (git-flow でいう main)
* main ブランチ ... 開発用のHEADブランチ (git-flowでいう develop)
* その他のトピックブランチ ... 機能追加・HOTFIXを行うブランチ。Pull Request にして main にマージする

## Commit Rules

* [Conventinal Commits](https://www.conventionalcommits.org/ja/v1.0.0/) に従ってください
* VS Code の場合 [vscode-conventional-commits](https://marketplace.visualstudio.com/items?itemName=vivaxy.vscode-conventional-commits) を使うと便利です

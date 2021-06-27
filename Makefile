# makefile

deploy:
	git checkout develop
	npm run standard-version
	git push --follow-tags origin develop
	git checkout main
	git merge develop
	git push -u origin main
	npm run release
	npm run deploy
	git checkout develop

.PHONY: clean
clean:
	@./gradlew clean

.PHONY: test
test: clean
	@./gradlew test
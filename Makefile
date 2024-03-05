run: 
	@./gradlew run

clean:
	@./gradlew clean

test: clean
	@./gradlew test

.PHONY: clean test

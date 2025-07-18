# === CONFIG ===
GRADLEW=./gradlew

# Run unit test
taskList:
	@echo "Task List"
	$(GRADLEW) task
	@echo "✅ Done!"

# Run unit test
clean:
	@echo "Clean build"
	$(GRADLEW) clean
	@echo "✅ Done!"

# Run unit test
unitTest:
	@echo "Unit test..."
	$(GRADLEW) testDebugUnitTest
	@echo "✅ Done!"

# Run UI test
uiTest:
	@echo "UI test..."
	$(GRADLEW) connectedAndroidTest
	@echo "✅ Done!"

# Run All test
allTest: clean unitTest uiTest
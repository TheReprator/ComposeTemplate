# === CONFIG ===
FLUTTER=./gradlew

# Run unit test
taskList:
	@echo "Task List"
	$(FLUTTER) task
	@echo "✅ Done!"

# Run unit test
unitTest:
	@echo "Unit test..."
	$(FLUTTER) testDebugUnitTest
	@echo "✅ Done!"

# Run UI test
uiTest:
	@echo "UI test..."
	$(FLUTTER) connectedAndroidTest
	@echo "✅ Done!"

# Run UI test
allTest: unitTest uiTest
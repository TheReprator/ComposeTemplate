# === CONFIG ===
FLUTTER=./gradlew

# Run unit test
unitTest:
	@echo "Unit test..."
	$(FLUTTER) testDebugUnitTest
	@echo "✅ Done!"
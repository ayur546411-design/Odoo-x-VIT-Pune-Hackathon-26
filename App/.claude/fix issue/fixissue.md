# Fix Issue Command

When given a bug report or issue, follow this workflow:

## Steps
1. **Read the issue** — understand the expected vs actual behavior
2. **Locate the code** — find the relevant files:
   - Approval bugs → `backend/app/services/approval_engine.py`
   - API bugs → `backend/app/routers/`
   - Currency bugs → `backend/app/services/currency_service.py`
   - OCR server bugs → `backend/app/services/ocr_service.py`
   - Android UI bugs → `android/app/src/main/java/.../ui/`
   - Android API bugs → `android/app/src/main/java/.../data/remote/`
   - Android OCR bugs → `android/app/src/main/java/.../ocr/`
3. **Write a failing test** that reproduces the bug
4. **Fix the code** with minimal changes
5. **Run the test suite** to confirm fix and no regressions:
   ```bash
   cd backend && pytest tests/ -v
   cd android && ./gradlew test
   ```
6. **Summarize** the root cause and fix

## Common Fixes
- Approval chain stuck → check `current_step` increment and `workflow_steps` ordering
- Currency shows NaN → check Exchange Rate API response parsing for null currencies
- OCR returns empty (Android) → check ML Kit InputImage creation and camera permissions
- OCR returns empty (Server) → check image preprocessing thresholds in `ocr_service.py`
- 403 on valid request → check role permission decorator matches the endpoint's intended audience
- Android crash on rotate → check ViewModel state preservation
- Retrofit 400 error → check DTO field names match Pydantic schema exactly
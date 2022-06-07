import * as assert from 'assert';
import * as vscode from 'vscode';
import * as extension from '../../extension';

suite('Extension Test Suite', () => {
	vscode.window.showInformationMessage('Start all tests.');

	const expectedMap = new Map();
	expectedMap.set(1, "Grant is buried in Grant's tomb.");
	expectedMap.set(3, "The sun rises in the east.");
	expectedMap.set(5, "$CATEGORY: Sample category");
	expectedMap.set(7, "Who's buried in Grant's tomb?");
	expectedMap.set(9, "Japanese characters originally came from what country? ");
	expectedMap.set(16, "Grant is ___ in Grant's tomb.");
	expectedMap.set(18, "$CATEGORY: Test category");
	expectedMap.set(20, "Japanese characters originally came from what country? ");
	expectedMap.set(27, "Who's buried in Grant's tomb?");

	test('Should return Map object with all captures and their lines from parsed file', async () => {
		const openPath = vscode.Uri.file("C:\\Users\\Hoffu\\Downloads\\forTests.gift");
		await vscode.workspace.openTextDocument(openPath).then(doc => {
			extension.textAdaptaion(doc.getText()).forEach((capture, line) => {
				assert.strictEqual(capture, expectedMap.get(line), "Test failed on [" + line + "] line");
			});
		});
	});
});

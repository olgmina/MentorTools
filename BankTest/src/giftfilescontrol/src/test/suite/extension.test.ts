import * as assert from 'assert';
import * as vscode from 'vscode';
import * as extension from '../../extension';

suite('Extension Test Suite', () => {
	vscode.window.showInformationMessage('Start all tests.');

	test('Should return new StatusBarItem', () => {
		const expectedButton = vscode.window.createStatusBarItem(vscode.StatusBarAlignment.Right, 130);
		expectedButton.command = 'giftfilescontrol.testCommand';
		expectedButton.text = 'Test button';
		const button = extension.createStatusBarButton('giftfilescontrol.testCommand', 'Test button', 130);
		assert.strictEqual(button.command, expectedButton.command, "Failed to create StatusBarItem");
		assert.strictEqual(button.text, expectedButton.text, "Failed to create StatusBarItem");
		assert.strictEqual(button.priority, expectedButton.priority, "Failed to create StatusBarItem");
	});
});

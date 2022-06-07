import * as assert from 'assert';
import * as vscode from 'vscode';
import * as vcs from '../../versionControlSystem';

suite('VersionControlSystem class test suite', () => {
	vscode.window.showInformationMessage('Start all tests.');

	test('Should push new TreeItems in data array', () => {
        const expectedArray = [
            new vcs.VersionTreeItem('Версия №11',
                [new vcs.VersionTreeItem("C:/Users/Hoffu/Downloads/GIFT-examples2_11.gift")]),
            new vcs.VersionTreeItem('Версия №12',
                [new vcs.VersionTreeItem("C:/Users/Hoffu/Downloads/GIFT-examples2_12.gift")]),
            new vcs.VersionTreeItem('Версия №13',
                [new vcs.VersionTreeItem("C:/Users/Hoffu/Downloads/GIFT-examples2_13.gift")])
        ];
		const vcsTreeDataProvider = new vcs.VCSTreeDataProvider([]);
        vcsTreeDataProvider.updateData([
            "C:/Users/Hoffu/Downloads/GIFT-examples2_11.gift",
            "C:/Users/Hoffu/Downloads/GIFT-examples2_12.gift",
            "C:/Users/Hoffu/Downloads/GIFT-examples2_13.gift"
        ]);
        vcsTreeDataProvider.data.forEach((treeItem) => {
            assert.strictEqual(treeItem.name, expectedArray.shift()?.name, 'Failed to push new TreeItems');
        });
	});
});

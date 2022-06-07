import * as fs from 'fs';
import path = require('path');
import * as vscode from 'vscode';
import { VCSTreeDataProvider, VersionTreeItem } from './versionControlSystem';

let _lastVersion: number = 0;

export function activate(context: vscode.ExtensionContext) {
	console.log('Congratulations, your extension "giftfilescontrol" is now active!');
	
	const commandMergeFilesName = 'giftfilescontrol.mergeFiles';
	const commandMergeFiles = vscode.commands.registerCommand(commandMergeFilesName, () => {
		vscode.window.showInformationMessage(
			"Do you really want to merge all opened files?",
			...["Yes", "No"]
		)
		.then((answer) => {
			if (answer === "Yes") {
				const editor = vscode.window.activeTextEditor;
				let newText: string = "";
				vscode.workspace.textDocuments.map((openDoc) => {
					if(openDoc.fileName.endsWith('.gift')) {
						newText += openDoc.getText();
						newText.trim();
						newText += "\n";
					}
				});
				if(editor && editor.document.fileName.endsWith('.gift')) {
					editor.edit((editBuilder) => {
						let firstLine = editor.document.lineAt(0);
						let lastLine = editor.document.lineAt(editor.document.lineCount - 1);
						let range = new vscode.Range(firstLine.range.start, lastLine.range.end);
						editBuilder.replace(range, newText);
					});
					vscode.window.showInformationMessage('Files merged successfully.');
				}
			}
		});
	});
	context.subscriptions.push(commandMergeFiles);
	createStatusBarButton(commandMergeFilesName, `$(files) Merge open GIFT files`, 120);

	const commandSaveVersionName = 'giftfilescontrol.saveFile';
	const commandSaveVersion = vscode.commands.registerCommand(commandSaveVersionName, () => {
		vscode.window.showInformationMessage(
			"Do you really want to save opened file?",
			...["Yes", "No"]
		)
		.then((answer) => {
			if (answer === "Yes") {
				const activeEditor = vscode.window.activeTextEditor;
				if(activeEditor && activeEditor.document.fileName.endsWith('.gift')) {
					const content = activeEditor.document.getText();
					const filePath = path.join(activeEditor.document.fileName.replace(new RegExp('.gift\$'), '')
						+ '_' + ++_lastVersion + '.gift');
					fs.writeFileSync(filePath, content, 'utf8');
					updateTreeViewData(vcsTreeDataProvider);
					vscode.window.showInformationMessage('New version of file was successfully saved.');
				}
			}
		});
	});
	context.subscriptions.push(commandSaveVersion);
	createStatusBarButton(commandSaveVersionName, `$(clone) Save current version`, 121);

	const vcsTreeDataProvider = new VCSTreeDataProvider([]);
	vscode.window.registerTreeDataProvider('vcs', vcsTreeDataProvider);
	updateTreeViewData(vcsTreeDataProvider);

	const switchVersion = vscode.commands.registerCommand('giftfilescontrol.switchVersion', (item: VersionTreeItem) => {
		if(!item.name.startsWith('Версия №')) {
			vscode.window.showInformationMessage(
				"Do you really want switch to previous version of this file?",
				...["Yes", "No"]
			)
			.then((answer) => {
				if (answer === "Yes") {
					const openPath = vscode.Uri.file(item.name);
					vscode.workspace.openTextDocument(openPath).then(doc => {
						const editor = vscode.window.activeTextEditor;
						if(editor) {
							editor.edit((editBuilder) => {
								let firstLine = editor.document.lineAt(0);
								let lastLine = editor.document.lineAt(editor.document.lineCount - 1);
								let range = new vscode.Range(firstLine.range.start, lastLine.range.end);
								editBuilder.replace(range, doc.getText());
							});
							vscode.window.showInformationMessage('Switch to previous version was successful.');
						}
					});
				}
			});	
		}
	});
	context.subscriptions.push(switchVersion);

	vscode.window.onDidChangeActiveTextEditor(() => {
		updateTreeViewData(vcsTreeDataProvider);
	});
}

export function deactivate() {}

export function createStatusBarButton(name: string, text: string, priority: number): vscode.StatusBarItem {
	const button = vscode.window.createStatusBarItem(vscode.StatusBarAlignment.Right, priority);
	button.command = name;
	button.text = text;
	button.show();
	return button;
}

function updateTreeViewData(vcsTreeDataProvider: VCSTreeDataProvider): void {
	const editor = vscode.window.activeTextEditor;
	if(editor) {
		let splittedPath = editor.document.fileName.replace(new RegExp('.gift\$'), '').split('\\');
		let fileName = splittedPath.pop();
		let filePath = '';
		splittedPath.forEach((part) => {
			filePath += part;
			filePath += '\\';
		});
		fs.readdir(filePath, (err, files: string[]) => {
			let paths: string[] = [];
			files.filter((file) => new RegExp(fileName + '_[0-9]*.gift').test(file)).forEach((file) => {
				const uri = vscode.Uri.file(file);
				paths.push(path.join(filePath + uri.path));
				let splitted = file.replace(/[^0-9_]*/g, '').split('_');
				_lastVersion = +splitted[splitted.length - 1] > _lastVersion ? +splitted[splitted.length - 1] : _lastVersion;
			});
			vcsTreeDataProvider.updateData(paths);
			vcsTreeDataProvider.refresh();
		});		
	}
}

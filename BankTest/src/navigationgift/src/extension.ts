import * as vscode from 'vscode';
import { NavigationProvider, TreeItem } from './navigation';

export function activate(context: vscode.ExtensionContext) {
	console.log('Congratulations, your extension "navigationgift" is now active!');
	const navigationProvider = new NavigationProvider(textAdaptaion(""));
    updateTreeView(navigationProvider);
	vscode.window.registerTreeDataProvider('navigation', navigationProvider);
	const goToLineCommand = vscode.commands.registerCommand('navigationgift.goToLine', (item: TreeItem) => {
		const editor = vscode.window.activeTextEditor;
		if(editor) {
			const range = editor.document.lineAt(item.line - 1).range;
			editor.revealRange(range);
			editor.selection = new vscode.Selection(range.start, range.end);
		}
	});
	context.subscriptions.push(goToLineCommand);
	vscode.window.onDidChangeTextEditorSelection(() => updateTreeView(navigationProvider));
	vscode.window.onDidChangeActiveTextEditor(() => updateTreeView(navigationProvider));
}

export function deactivate() {}

export function textAdaptaion(documentText: string): Map<number, string> {
	let splitted = documentText.split(/\n\s*\n/);
	splitted = splitted.map(
		(capture) => capture.split(/\n/)
			.filter((line) => !line.startsWith('//'))
			.reduce((section, line) => section + '\n' + line, '')
			.trim()
	);
	splitted = splitted.filter((capture) => capture !== '');

	let linesAndQuestions = new Map();
	documentText.split(/\n/).forEach((line, i) => {
		for(let n = 0; n < splitted.length; n++) {
			if(line.startsWith(splitted[n]) || splitted[n].startsWith(line)) {
				linesAndQuestions.set(i + 1, splitted[n]
					.replace(/{[\s\S]*}/, '___')
					.replace(/___$/, '')
					.replace(/(?<=::[\s\S]*::)[\s\S]*/, '')
					.replace(/::/g, '')
				);
				splitted.splice(n, 1);
				break;
			}
		}
	});
	return linesAndQuestions;
}

function updateTreeView(navigationProvider: NavigationProvider): void {
	const activeEditor = vscode.window.activeTextEditor;
	navigationProvider.updateData(
		(activeEditor && activeEditor.document.fileName.endsWith('.gift'))
		? textAdaptaion(activeEditor.document.getText())
		: textAdaptaion('')
	);
	navigationProvider.refresh();
}
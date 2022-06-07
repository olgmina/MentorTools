import * as vscode from 'vscode';
import { templateFactory } from './template';

export function activate(context: vscode.ExtensionContext) {
	const options: string[] = ["Множественный выбор",
		"Множественный выбор с множественными ответами",
		"Правда-Ложь", "Короткий ответ", "Сопоставление",
		"Пропущенное слово", "Числовой ответ", "Эссе"];
	const placeHolderText: string = 'Шаблон какого типа вопроса вы хотите вставить?';
	const templateCommandName: string = 'gifttemplate.choiceTheTemplate';

	const templateCommand: vscode.Disposable = vscode.commands.registerCommand(templateCommandName, () => {
		showQuickPick(options, placeHolderText).then(result => {
			const editor = vscode.window.activeTextEditor;
			if(editor) {
				editor.edit((editBuilder) => {
					const position: vscode.Position = editor.selection.active;
					editBuilder.insert(position, templateFactory(result));
				});
			}
		});
	});
	context.subscriptions.push(templateCommand);
	const statusBarButton: vscode.StatusBarItem = vscode.window.createStatusBarItem(vscode.StatusBarAlignment.Right, 150);
	statusBarButton.command = templateCommandName;
	statusBarButton.text = `$(output) Вставить шаблон для вопроса`;
	statusBarButton.show();
}

export function deactivate() {}

async function showQuickPick(options: string[], placeHolderText: string): Promise<string> {
	const result = await vscode.window.showQuickPick(options, {
		placeHolder: placeHolderText
	});
	return result ? result : '';
}
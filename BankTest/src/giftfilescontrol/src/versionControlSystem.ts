import * as vscode from 'vscode';

export class VCSTreeDataProvider implements vscode.TreeDataProvider<VersionTreeItem> {
  private _onDidChangeTreeData: vscode.EventEmitter<VersionTreeItem | undefined | void> = new vscode.EventEmitter<VersionTreeItem | undefined | void>();
  readonly onDidChangeTreeData: vscode.Event<VersionTreeItem | undefined | void> = this._onDidChangeTreeData.event;

  data: VersionTreeItem[] = [];

  constructor(files: string[]) {
    this.updateData(files);
  }

  refresh(): void {
		this._onDidChangeTreeData.fire();
  }

  getTreeItem(element: VersionTreeItem): vscode.TreeItem|Thenable<vscode.TreeItem> {
    return element;
  }

  getChildren(element?: VersionTreeItem|undefined): vscode.ProviderResult<VersionTreeItem[]> {
    if (element === undefined) {
      return this.data;
    }
    return element.children;
  }

  updateData(files: string[]): void {
    this.data = [];
    const filesAndVersions = new Map<string, number>();
    files.map((file) => {
      const splitted = file.replace(/[^0-9_]*/g, '').split('_');
      const version = splitted[splitted.length - 1];
      filesAndVersions.set(file, +version);
    });
    const sortedFiles = new Map([...filesAndVersions.entries()].sort((a, b) => a[1] - b[1]));
    sortedFiles.forEach((version, path) => {
      this.data.push(new VersionTreeItem('Версия №' + version, [new VersionTreeItem(path)]));
    });
    this.refresh();
  }
}

export class VersionTreeItem extends vscode.TreeItem {
  children: VersionTreeItem[]|undefined;
  name: string;

  command = {
    "title": "Switch file version",
    "command": "giftfilescontrol.switchVersion",
    "arguments": [this]
  };

  constructor(label: string, children?: VersionTreeItem[]) {
    super(
        label,
        children === undefined ? vscode.TreeItemCollapsibleState.None :
                                 vscode.TreeItemCollapsibleState.Expanded);
    this.children = children;
    this.name = label;
  }
}
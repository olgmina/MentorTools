import * as vscode from 'vscode';

export class NavigationProvider implements vscode.TreeDataProvider<TreeItem> {
  private _onDidChangeTreeData: vscode.EventEmitter<TreeItem | undefined | void> = new vscode.EventEmitter<TreeItem | undefined | void>();
	readonly onDidChangeTreeData: vscode.Event<TreeItem | undefined | void> = this._onDidChangeTreeData.event;

  data: TreeItem[] = [];

  constructor(headers: Map<number, string>) {
    this.updateData(headers);
  }

  refresh(): void {
		this._onDidChangeTreeData.fire();
	}

  getTreeItem(element: TreeItem): vscode.TreeItem|Thenable<vscode.TreeItem> {
    return element;
  }

  getChildren(element?: TreeItem|undefined): vscode.ProviderResult<TreeItem[]> {
    if (element === undefined) {
      return this.data;
    }
    return element.children;
  }

  updateData(headers: Map<number, string>): void {
    let categoriesLines = this.searchForCategories(headers);
    if(categoriesLines.size !== 0) {
      categoriesLines = this.searchForEndLines(categoriesLines, headers);
      
      const array = Array.from(categoriesLines.keys());
      headers.forEach((capture, line) => {
        if(line < array[0][0]) {this.data.push(new TreeItem(capture, line));}
      });

      this.parseCategories(categoriesLines, headers);
    } else {
      headers.forEach((capture, line) => this.data.push(new TreeItem(capture, line)));
    }
  }

  searchForCategories(headers: Map<number, string>): Map<number[], string> {
    this.data = [];
    const categoriesLines = new Map<number[], string>();
    headers.forEach((capture, lines) => {
      if(capture.startsWith('$CATEGORY:')) {
        categoriesLines.set([lines], capture);
      }
    });
    return categoriesLines;
  }

  searchForEndLines(categoriesLines: Map<number[], string>, headers: Map<number, string>): Map<number[], string> {
    let endLines: number[] = [];
    categoriesLines.forEach((capture, lines) => endLines.push(lines[0] - 1));
    endLines.shift();
    const arr: number[] = Array.from(headers.keys());
    endLines.push(arr[arr.length - 1] + 1);
    categoriesLines.forEach((capture, lines) => {
      let endLine = endLines.shift();
      if(endLine) {lines.push(endLine);}
    });
    return categoriesLines;
  }

  parseCategories(categoriesLines: Map<number[], string>, headers: Map<number, string>): void {
    let child = new Map<TreeItem, string[]>();
    for(let i = this.getCount(categoriesLines); i > 0; i--) {
      categoriesLines.forEach((capture, linesOfCategory) => {
        let subCategories: TreeItem[] = [];
        if(capture.split('/').length === i) {
          subCategories = this.getLowLevelTreeItemsArray(linesOfCategory, headers, subCategories);
          const splitted = capture.split('/')[i - 1];
          let items: TreeItem[] = [];
          child.forEach((parent, item) => {
            if(splitted.startsWith(parent[0])) {
              items.push(item);
              child.delete(item);
            }
          });
          const treeItem = new TreeItem(splitted.replace('$CATEGORY: ', '') + " (" + subCategories.length + ")", linesOfCategory[0], [...items, ...subCategories]);
          if(capture.split('/')[i - 2] === undefined) {this.data.push(treeItem);}
          else {child.set(treeItem, [capture.split('/')[i - 2], capture]);}
        }
      });
    }
    child.forEach((parent, item) => {
      const treeItem = new TreeItem(parent[1].replace('$CATEGORY: ', '') + " (" + item.children?.length + ")", item.line, item.children);
      this.data.push(treeItem);
    });
  }

  getCount(categoriesLines: Map<number[], string>): number {
    let count: number = 0;
    categoriesLines.forEach((category) => {
      let length = category.split('/').length;
      count = length > count ? length : count;
    });
    return count;
  }

  getLowLevelTreeItemsArray(lines: number[], headers: Map<number, string>, items: TreeItem[]): TreeItem[] {
    for(let i = lines[0] + 1; i < lines[1]; i++) {
      let header = headers.get(i);
      if(header) {items.push(new TreeItem(header, i));}
    }
    return items;
  }
}

export class TreeItem extends vscode.TreeItem {
  children: TreeItem[]|undefined;
  line: number;

  command = {
    "title": "Go to line",
    "command": "navigationgift.goToLine",
    "arguments": [this]
  };

  constructor(label: string, line: number, children?: TreeItem[]) {
    super(
        label,
        children === undefined ? vscode.TreeItemCollapsibleState.None :
                                 vscode.TreeItemCollapsibleState.Expanded);
    this.children = children;
    this.line = line;
  }
}
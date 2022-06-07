import * as assert from 'assert';
import * as vscode from 'vscode';
import * as navigation from '../../navigation';

suite('Navigation class test suite', () => {
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

	test('Should return Map object with only captures starts with "$CATEGORY:"', () => {
		const navigationProvider = new navigation.NavigationProvider(new Map());
		navigationProvider.searchForCategories(expectedMap).forEach((capture) => {
			assert.strictEqual(capture.startsWith('$CATEGORY:'), true, "Test failed on [" + capture + "] capture");
		});
	});

	test('Should return Map object with captures starts with "$CATEGORY:" and their lines range', () => {
		const testArray = [5, 18];
		const testArray2 = [17, 28];
		const navigationProvider = new navigation.NavigationProvider(new Map());
		const categoriesAndLines = navigationProvider.searchForCategories(expectedMap);
		navigationProvider.searchForEndLines(categoriesAndLines, expectedMap).forEach((category, lines) => {
			assert.strictEqual(lines[0], testArray.shift(), "Test failed on [" + category + "] category");
			assert.strictEqual(lines[1], testArray2.shift(), "Test failed on [" + category + "] category");
		});
	});

	test('Should return number of maximum subcategories', () => {
		const testAmount = 1;
		const navigationProvider = new navigation.NavigationProvider(new Map());
		const categoriesAndLines = navigationProvider.searchForCategories(expectedMap);
		const categoriesWithRange = navigationProvider.searchForEndLines(categoriesAndLines, expectedMap);
		assert.strictEqual(navigationProvider.getCount(categoriesWithRange), testAmount, "Counting subcategories failed");
	});

	test('Should return array from parameters with new TreeItems within it, TreeItems picks from specific range', () => {
		const testArray = [5, 17];
		const expectedArray: navigation.TreeItem[] = [
			new navigation.TreeItem("Who's buried in Grant's tomb?", 7),
			new navigation.TreeItem("Japanese characters originally came from what country? ", 9),
			new navigation.TreeItem("Grant is ___ in Grant's tomb.", 16)
		];
		const navigationProvider = new navigation.NavigationProvider(new Map());
		const treeItems: navigation.TreeItem[] = [];
		navigationProvider.getLowLevelTreeItemsArray(testArray, expectedMap, treeItems);
		expectedArray.forEach((expectedItem) => {
			assert.notStrictEqual(treeItems.shift(), expectedItem, "Making TreeItem array failed");
		});
	});

	test('Should hand over TreeItems structure into "data" array without orphan elements', () => {
		const testArray = [5, 7, 9, 16, 18, 20, 27];
		const navigationProvider = new navigation.NavigationProvider(new Map());
		const categoriesAndLines = navigationProvider.searchForCategories(expectedMap);
		const categoriesWithRange = navigationProvider.searchForEndLines(categoriesAndLines, expectedMap);
		navigationProvider.parseCategories(categoriesWithRange, expectedMap);
		navigationProvider.data.forEach((item) => {
			checkTreeItemChildren(item, testArray);
		});
	});

	const checkTreeItemChildren = (treeItem: navigation.TreeItem, testArray: number[]): void => {
		assert.strictEqual(treeItem.line, testArray.shift(), "Pushing TreeItems structure in data was failed");
		if(treeItem.children) {
			treeItem.children.forEach((child) => {
				checkTreeItemChildren(child, testArray);
			});
		}
	};

	test('Should hand over TreeItems structure into "data" array', () => {
		const testArray = Array.from(expectedMap.keys());
		const navigationProvider = new navigation.NavigationProvider(new Map());
		navigationProvider.updateData(expectedMap);
		navigationProvider.data.forEach((item) => {
			checkTreeItemChildren(item, testArray);
		});
	});
});

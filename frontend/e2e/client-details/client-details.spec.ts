import {expect, test} from '@playwright/test';

test.describe('Client details', () => {
  test('should show the correct information', async ({page}) => {
    await page.goto('/');
    await page.getByRole('cell', {name: 'John Snow'}).click();
    await expect(page.getByRole('textbox', {name: 'Display Name'})).toHaveValue('John S.');
    await expect(page.getByRole('checkbox', {name: 'Active'})).not.toBeChecked();
    await expect(page.getByRole('textbox', {name: 'Location'})).toHaveValue('The North, Westeros');
    await expect(page.getByRole('textbox', {name: 'Details'})).toHaveValue('Military commander with leadership experience, skilled in battle tactics and diplomacy.');
  });

  test('delete should not delete if cancel is pressed', async ({page}) => {
    await page.goto('/');
    await page.getByRole('cell', {name: 'John Snow'}).click();
    await page.getByTestId('DELETE_CLIENT_BUTTON').click();
    await page.getByRole('button', {name: 'Cancel'}).click();
    await expect(page.getByLabel('Client details')).toBeVisible();
  });

  test('Cancel button should close the edit details popup', async ({page}) => {
    await page.goto('/');
    await page.getByRole('cell', {name: 'John Snow'}).click();
    await page.getByTestId('CLOSE_DETAILS_BUTTON').click();
    await expect(page.getByLabel('Client details')).toBeHidden()
  });

  test('delete should remove the client', async ({page}) => {
    await page.goto('/');
    await page.getByRole('cell', {name: 'Harry Potter'}).click();
    await page.getByTestId('DELETE_CLIENT_BUTTON').click();
    await page.getByRole('button', {name: 'Yes'}).click();

    await expect(page.getByLabel('Client details')).toBeHidden();
    await page.getByRole('textbox', {name: 'Search clients'}).fill('Harry');
    await expect(page.locator('tbody tr')).toHaveCount(0);
  });

  test('Edit client should change the details', async ({page}) => {
    const displayNameEdit = 'Peter P. EDIT';
    const locationEdit = 'New York, EDIT';
    await page.goto('/');
    await page.getByRole('cell', {name: 'Peter Parker'}).click();
    await page.getByRole('textbox', {name: 'Display Name'}).fill(displayNameEdit);
    await page.getByRole('textbox', {name: 'Location'}).fill(locationEdit);
    await page.getByRole('checkbox', {name: 'Active'}).uncheck();
    await page.getByTestId('EDIT_CLIENT_BUTTON').click();

    await expect(page.locator('tbody')).toContainText(displayNameEdit);
    await expect(page.locator('tbody')).toContainText(locationEdit);
  });

});

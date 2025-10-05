import {expect, test} from '@playwright/test';

test.describe('Add a new client', () => {
  test('should create a new client and insert it in the table', async ({page}) => {
    await page.goto('/');
    await page.getByTestId('NEW_CLIENT_BUTTON').click();
    await page.getByRole('textbox', {name: 'Full Name'}).fill('PLAY FULL NAME');
    await page.getByRole('textbox', {name: 'Email'}).fill('PLAY@EMAIL.com');
    await page.getByRole('textbox', {name: 'Display Name'}).fill('PLAY DISPLAY NAME');
    await page.getByRole('checkbox', {name: 'Active'}).check();
    await page.getByRole('textbox', {name: 'Location'}).fill('PLAY LOCATION');
    await page.getByRole('textbox', {name: 'Details'}).fill('PLAY DETAILS');
    await page.getByTestId('SAVE_CLIENT_FORM').click();

    await expect(page.locator('tbody')).toContainText('PLAY FULL NAME');
    await expect(page.locator('tbody')).toContainText('PLAY DISPLAY NAME');
    await expect(page.locator('tbody')).toContainText('PLAY@EMAIL.com');
    await expect(page.locator('tbody')).toContainText('PLAY LOCATION');
  });

  test('should not allow duplicate email addresses', async ({page}) => {
    await page.goto('/');
    await page.getByTestId('NEW_CLIENT_BUTTON').click();
    await page.getByRole('textbox', {name: 'Full Name'}).fill('PLAY FULL NAME');
    await page.getByRole('textbox', {name: 'Email'}).fill('john@wick.com');
    await page.getByRole('textbox', {name: 'Display Name'}).fill('PLAY DISPLAY NAME');
    await page.getByRole('checkbox', {name: 'Active'}).check();
    await page.getByTestId('SAVE_CLIENT_FORM').click();

    await expect(page.getByTestId('BACKEND_VALIDATION_ERROR')).toContainText('This email is already used');
  });
});

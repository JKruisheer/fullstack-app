import {expect, test} from '@playwright/test';

test.describe('Client overview', () => {
  test('should be able to sort based on full name', async ({page}) => {
    await page.goto('/');
    await page.getByRole('columnheader', {name: 'Full name'}).click();
    await expect(page.locator('tbody tr').first().locator('td').nth(0)).toHaveText('Bruce Wayne');

    await page.getByRole('columnheader', {name: 'Full name'}).click();
    await expect(page.locator('tbody tr').first().locator('td').nth(0)).toHaveText('Tony Stark');
  });

  test('should be able to sort based on display name', async ({page}) => {
    await page.goto('/');
    await page.getByRole('columnheader', {name: 'Display name'}).click();
    await expect(page.locator('tbody tr').first().locator('td').nth(1)).toHaveText('Bruce W.');

    await page.getByRole('columnheader', {name: 'Display name'}).click();
    await expect(page.locator('tbody tr').first().locator('td').nth(1)).toHaveText('Tony S.');
  });

  test('should be able to sort based on Email name', async ({page}) => {
    await page.goto('/');
    await page.getByRole('columnheader', {name: 'Email'}).click();
    await expect(page.locator('tbody tr').first().locator('td').nth(2)).toHaveText('Bruce@wayne.com');

    await page.getByRole('columnheader', {name: 'Email'}).click();
    await expect(page.locator('tbody tr').first().locator('td').nth(2)).toHaveText('Tony@stark.com');
  });

  test('should be able to sort based on Location', async ({page}) => {
    await page.goto('/');
    await page.getByRole('columnheader', {name: 'Location'}).click();
    await expect(page.locator('tbody tr').first().locator('td').nth(3)).toHaveText('');

    await page.getByRole('columnheader', {name: 'Location'}).click();
    await expect(page.locator('tbody tr').first().locator('td').nth(3)).toHaveText('Themyscira');
  });

  test('should be able to search for records', async ({page}) => {
    await page.goto('/');
    await page.getByRole('textbox', {name: 'Search clients'}).click();
    await page.getByRole('textbox', {name: 'Search clients'}).fill('John Sn');
    await expect(page.locator('tbody tr').first().locator('td').nth(0)).toHaveText('John Snow');
  });
});

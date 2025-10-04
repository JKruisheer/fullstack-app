import {App} from './app';
import {createComponentFactory, Spectator} from '@ngneat/spectator';

describe('App', () => {
  let spectator: Spectator<App>;
  const createComponent = createComponentFactory({
    component: App
  });

  it('should create the app', () => {
    spectator = createComponent();
    expect(spectator.component).toBeTruthy();
  });

  it('should render title', () => {
    spectator = createComponent();
    expect(spectator.query('h1')?.textContent).toContain('Client management');
  });

  it('should render a toast', () => {
    spectator = createComponent();
    const toast = spectator.query('p-toast');
    expect(toast).toBeTruthy();
    expect(toast?.getAttribute('key')).toBe('bottom-right-toast');
    expect(toast?.getAttribute('position')).toBe('bottom-right');
  });
});

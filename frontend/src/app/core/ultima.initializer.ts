import { Injector } from '@angular/core';
import { LOGGER } from '@app/services/logger.service';
import { ConfigService } from '@app/services/config.service';
import { addSplashItem } from '@app/core/core.initializer';

export async function init(injector: Injector): Promise<void> {
  LOGGER.info('Initializing Ultima');

  addSplashItem('Initializing Ultima');
  const config: ConfigService = injector.get(ConfigService);
  await config.load('layout', 'assets/config/layout-config.json');
}

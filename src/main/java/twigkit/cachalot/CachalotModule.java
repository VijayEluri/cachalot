/*
 * Copyright 2010 Hjortur Stefan Olafsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package twigkit.cachalot;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import net.sf.ehcache.CacheManager;

import java.net.URL;

/**
 * The Cachalot module instantiates the cache, and decorates all methods annotated
 * with the {@link Cache} annotation with the a {@link CacheInterceptor}.
 *
 * @author mr.olafsson
 */
@Singleton
public class CachalotModule extends AbstractModule {

    private final CacheManager cacheManager;

    public CachalotModule() {
        cacheManager = CacheManager.newInstance();
    }

    public CachalotModule(String cacheName) {
        cacheManager = CacheManager.newInstance(cacheName);
    }

    public CachalotModule(URL cacheURL) {
        cacheManager = CacheManager.newInstance(cacheURL);
    }

    @Override
	protected void configure() {
        CacheInterceptor ci = new CacheInterceptor();
        requestInjection(ci);

        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Cache.class), ci);
    }

    @Provides
    @Cachalot
    public CacheManager getCacheManager() {
        return cacheManager;
    }
}

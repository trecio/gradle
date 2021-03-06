/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.externalresource.local;

import org.gradle.api.Transformer;
import org.gradle.internal.filestore.FileStoreSearcher;
import org.gradle.internal.Factory;
import org.gradle.internal.resource.local.LocallyAvailableResource;
import org.gradle.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Makes a LocallyAvailableResourceFinder out of a FileStoreSearcher.
 * @param <C> The type of criterion the filestore can be searched for, and therefore locally available resources searched for.
 */
public class LocallyAvailableResourceFinderSearchableFileStoreAdapter<C> extends AbstractLocallyAvailableResourceFinder<C> {

    public LocallyAvailableResourceFinderSearchableFileStoreAdapter(final FileStoreSearcher<C> fileStore) {
        super(new Transformer<Factory<List<File>>, C>() {
            public Factory<List<File>> transform(final C criterion) {
                return new Factory<List<File>>() {
                    public List<File> create() {
                        Set<? extends LocallyAvailableResource> entries = fileStore.search(criterion);
                        return CollectionUtils.collect(entries, new ArrayList<File>(entries.size()), new Transformer<File, LocallyAvailableResource>() {
                            public File transform(LocallyAvailableResource original) {
                                return original.getFile();
                            }
                        });
                    }
                };
            }
        });
    }

    
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.netbeans.modules.websvc.jaxws;

import java.io.File;
import org.netbeans.junit.NbTestCase;
import org.netbeans.modules.websvc.jaxws.api.JAXWSSupport;
import org.netbeans.modules.websvc.jaxws.spi.JAXWSSupportProvider;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.LocalFileSystem;
import org.openide.filesystems.Repository;
import org.openide.util.Lookup;

/**
 *
 * @author Lukas Jungmann
 */
public class CustomJAXWSSupportProviderTest extends NbTestCase {
    
    private FileObject datadir;
    private FileObject ws;
    private FileObject nows;
    
    static {
        CustomJAXWSSupportProviderTest.class.getClassLoader().setDefaultAssertionStatus(true);
    }
    
    /** Creates a new instance of CustomJAXWSSupportProviderTest */
    public CustomJAXWSSupportProviderTest(String name) {
        super(name);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        File f = getWorkDir();
        assertTrue("work dir exists", f.exists());
        LocalFileSystem lfs = new LocalFileSystem ();
        lfs.setRootDirectory (f);
        Repository.getDefault ().addFileSystem (lfs);
        datadir = FileUtil.toFileObject(f);
        assertNotNull("no FileObject", datadir);
        ws = datadir.createData("custom", "ws");
        assertNotNull("no ws FileObject", ws);
        nows = datadir.createData("custom", "nows");
        assertNotNull("no ws FileObject", nows);
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        ws.delete();
        nows.delete();
    }
    
    public void testProviders() throws Exception {
        Lookup.Result<JAXWSSupportProvider> res = Lookup.getDefault().lookup(new Lookup.Template<JAXWSSupportProvider>(JAXWSSupportProvider.class));
        assertEquals("there should be 2 instances - one from websvc/jaxwsapi and one from tests", 2, res.allInstances ().size ());
    }
    
    public void testGetJAXWSSupport() throws Exception {
        JAXWSSupport ws1 = JAXWSSupport.getJAXWSSupport(ws);
        assertNotNull("found ws support", ws1);
        JAXWSSupport ws2 = JAXWSSupport.getJAXWSSupport(nows);
        assertNull("not found ws support", ws2);
    }
}

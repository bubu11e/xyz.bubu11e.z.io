/*
 * Copyright 2015 Julien Girard 
 *
 *    Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 ;
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://http://www.gnu.org/licenses/gpl-3.0.html
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package xyz.bubu11e.z.io.core.dw;

import java.util.Random;

import org.junit.*;
import org.junit.Assert.*;

public class DescriptorWordTest {

  private DescriptorWord dw = null;
  private byte[] dwBuffer = null;

  @BeforeClass
  public static void oneTimeSetUp() {
  }

  @AfterClass
  public static void oneTimeTearDown() {
  }

  @Before
  public void setUp() {
    dwBuffer = new byte[4];
    dw = new DescriptorWord();
  }

  @After
  public void tearDown() {
    dw = null;
    dwBuffer = null;
  }

  @Test (expected = DescriptorWordException.class)
  public void bufferNotSet() throws DescriptorWordException {
    dw.getBuffer();
  }

  @Test
  public void bufferIsSet() throws DescriptorWordException {
    (new Random()).nextBytes(dwBuffer);
    dw.setBuffer(dwBuffer);
    Assert.assertArrayEquals(dwBuffer, dw.getBuffer());
  }
}
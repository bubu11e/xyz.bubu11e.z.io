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

package io.julien_g.z.io.core.dw;

import org.junit.*;
import org.junit.Assert.*;

public class BlockDescriptorWordTest {

  private BlockDescriptorWord dw = null;
  private byte[] array = null;

  @BeforeClass
  public static void oneTimeSetUp() {
  }

  @AfterClass
  public static void oneTimeTearDown() {
  }

  @Before
  public void setUp() {
    dw = new BlockDescriptorWord();
    array = new byte[4];
  }

  @After
  public void tearDown() {
    dw = null;
    array = null;
  }

  @Test
  public void getValidSegmentLength() throws DescriptorWordException {
    array[0] = (byte)0x00;
    array[1] = (byte)0xff;
    array[2] = (byte)0x00;
    array[3] = (byte)0x00;

    dw.setBuffer(array);
    Assert.assertEquals(dw.getSegmentLength(), 255);
  }

  @Test
  public void getValidSegmentLength2() throws DescriptorWordException {
    array[0] = (byte)0x80;
    array[1] = (byte)0xff;
    array[2] = (byte)0xff;
    array[3] = (byte)0xff;

    dw.setBuffer(array);
    Assert.assertEquals(dw.getSegmentLength(), 16777215);
  }

  @Test (expected = DescriptorWordException.class)
  public void getTooLargeSegmentLength() throws DescriptorWordException {
    array[0] = (byte)0x7f;
    array[1] = (byte)0xff;
    array[2] = (byte)0x00;
    array[3] = (byte)0x00;

    dw.setBuffer(array);
    dw.getSegmentLength();
  }

  @Test (expected = DescriptorWordException.class)
  public void getTooSmallSegmentLength() throws DescriptorWordException {
    array[0] = (byte)0x00;
    array[1] = (byte)0x00;
    array[2] = (byte)0x00;
    array[3] = (byte)0x00;

    dw.setBuffer(array);
    dw.getSegmentLength();
  }

  @Test
  public void setValidSegmentLength() throws DescriptorWordException {
    dw.setBuffer(array);
    dw.setSegmentLength(255, true);
    byte[] expected = {(byte)0x80, 0x00, 0x00, (byte)0xff};
    Assert.assertArrayEquals(array, expected);
  }

  @Test
  public void setValidSegmentLength2() throws DescriptorWordException {
    dw.setBuffer(array);
    dw.setSegmentLength(255, false);
    byte[] expected = {0x00, (byte)0xff, 0x00, 0x00};
    Assert.assertArrayEquals(array, expected);
  }

  @Test (expected = DescriptorWordException.class)
  public void setTooLargeSegmentLength() throws DescriptorWordException {
    dw.setBuffer(array);
    dw.setSegmentLength(32761, false);
  }

  @Test (expected = DescriptorWordException.class)
  public void setTooSmallSegmentLength() throws DescriptorWordException {
    dw.setBuffer(array);
    dw.setSegmentLength(0, true);
  }
}
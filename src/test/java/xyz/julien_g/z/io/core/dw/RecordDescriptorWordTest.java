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

package xyz.julien_g.z.io.core.dw;

import org.junit.*;
import org.junit.Assert.*;

public class RecordDescriptorWordTest {

  private RecordDescriptorWord dw = null;
  private byte[] array = null;

  @BeforeClass
  public static void oneTimeSetUp() {
  }

  @AfterClass
  public static void oneTimeTearDown() {
  }

  @Before
  public void setUp() {
    dw = new RecordDescriptorWord();
    array = new byte[4];
  }

  @After
  public void tearDown() {
    dw = null;
    array = null;
  }

  @Test
  public void getValidSegmentLength() throws DescriptorWordException {
    array = new byte[] {(byte)0x00, (byte)0xff, (byte)0x00, (byte)0x00};
    dw.setBuffer(array);
    Assert.assertEquals(dw.getSegmentLength(), 255);
  }

  @Test
  public void getValidSegmentLength2() throws DescriptorWordException {
    array = new byte[] {(byte)0x7f, (byte)0xf8, (byte)0x00, (byte)0x00};
    dw.setBuffer(array);
    Assert.assertEquals(dw.getSegmentLength(), 32760);
  }

  @Test (expected = DescriptorWordException.class)
  public void getTooLargeSegmentLength() throws DescriptorWordException {
    array = new byte[] {(byte)0xff, (byte)0xff, (byte)0x00, (byte)0x00};
    dw.setBuffer(array);
    dw.getSegmentLength();
  }

  @Test (expected = DescriptorWordException.class)
  public void getTooSmallSegmentLength() throws DescriptorWordException {
    array = new byte[] {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
    dw.setBuffer(array);
    dw.getSegmentLength();
  }

  @Test
  public void getValidSegmentCode() throws DescriptorWordException {
    array = new byte[] {(byte)0x00, (byte)0xff, (byte)0x03, (byte)0x00};
    dw.setBuffer(array);
    Assert.assertEquals(dw.getSegmentCode(), RecordDescriptorWord.Code.OTHER_SEGMENT);
  }

  @Test (expected = DescriptorWordException.class)
  public void getInvalidSegmentCode() throws DescriptorWordException {
    array = new byte[] {(byte)0x00, (byte)0xff, (byte)0x04, (byte)0x00};
    dw.setBuffer(array);
    dw.getSegmentCode();
  }

  @Test
  public void getValidLastByte() throws DescriptorWordException {
    array = new byte[] {(byte)0x00, (byte)0xff, (byte)0x00, (byte)0x00};
    dw.setBuffer(array);
    Assert.assertTrue(dw.isLastByteValid());
  }

  @Test
  public void getInvalidLastByte() throws DescriptorWordException {
    array = new byte[] {(byte)0x00, (byte)0xff, (byte)0x00, (byte)0x01};
    dw.setBuffer(array);
    Assert.assertFalse(dw.isLastByteValid());
  }

  @Test
  public void setValidSegmentLength() throws DescriptorWordException {
    dw.setBuffer(array);
    dw.setSegmentLength((short)255);
    byte[] expected = new byte[] {(byte)0x00, (byte)0xff, (byte)0x00, (byte)0x00};
    Assert.assertArrayEquals(array, expected);
  }

  @Test (expected = DescriptorWordException.class)
  public void setTooLargeSegmentLength() throws DescriptorWordException {
    dw.setBuffer(array);
    dw.setSegmentLength((short)32761);
  }

  @Test (expected = DescriptorWordException.class)
  public void setTooSmallSegmentLength() throws DescriptorWordException {
    dw.setBuffer(array);
    dw.setSegmentLength((short)0);
  }

  @Test
  public void setValidSegmentCode() throws DescriptorWordException {
    dw.setBuffer(array);
    dw.setSegmentCode(RecordDescriptorWord.Code.FIRST_SEGMENT);
    byte[] expected = new byte[] {(byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00};
    Assert.assertArrayEquals(array, expected);
  }

  @Test
  public void setValidLastByte() throws DescriptorWordException {
    array = new byte[] {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01};
    dw.setBuffer(array);
    dw.setLastByte();
    byte[] expected = new byte[] {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
    Assert.assertArrayEquals(array, expected);
  }
}

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

/**
 * This class is used to read IBM BDW.
 * 
 * The BDW is a 4 bytes length array.
 *
 * There are two types of BDW :
 * If bit 0 is zero, BDW is call nonextended. Bit 1-15
 * contains the block length. others are zeroes.
 *
 * If bit 0 is one, BDW is call extended. Bit 1-31
 * contains the block length.
 */
public class BlockDescriptorWord extends DescriptorWord {

    /** Maximum length of a segment in nonextended mode. */
	public final static int BDW_MAX_SEGMENT_LENGTH_NONEXTENDED = 32760;	
	
  /**
   * Check the length of the record/segment.
   * @param length Length of the record/segment.
   * @throws A DescriptorWordException if the length of the segment is an invalid value.
   */
  private static void checkSegmentLength(int length, boolean extended) throws DescriptorWordException {

    if(length < DescriptorWord.DESCRIPTOR_WORD_LENGTH) {
      throw new DescriptorWordException("Invalid segment length: "+length
        +". Length must be at least " + DescriptorWord.DESCRIPTOR_WORD_LENGTH + ".");
    }

    if(!extended && length > BDW_MAX_SEGMENT_LENGTH_NONEXTENDED) {
      throw new DescriptorWordException("Invalid segment length: "+length
        +". Length must be less than " + BDW_MAX_SEGMENT_LENGTH_NONEXTENDED 
        + " in nonextended mode	.");
    }

    return;
  }

  /**
   * Getter for the segment type.
   * @return True if the segment type is extended, false otherwise.
   * @throws DescriptorWordException If the buffer is not set.
   */
  private boolean getExtended() throws DescriptorWordException {
    return (this.getBuffer()[this.getOffset()] & (byte)0x80) == 0 ? false : true;
  }

  /**
   * Getter for the segment length.
   * @return A short containing the segment length.
   * @throws DescriptorWordException If the length of the segment is an invalid value or the buffer is not set.
   */
  public int getSegmentLength() throws DescriptorWordException {
    int length = 0;

    if(this.getExtended()) {
      length = (this.getBuffer()[this.getOffset()] & 0x7f) << 24 
        | (this.getBuffer()[this.getOffset() + 1] & 0xff) << 16 
        | (this.getBuffer()[this.getOffset() + 2] & 0xff) << 8 
        | (this.getBuffer()[this.getOffset() + 3] & 0xff);
    } else {
      length = this.getBuffer()[this.getOffset()] << 8 
      | (this.getBuffer()[this.getOffset() + 1] & 0xff);
    }

    this.checkSegmentLength(length, this.getExtended());

    return length;
  }

  /**
   * Setter for the segment length.
   * @param length Length of the record/segment to set.
   * @param extended true if the block is extended, false otherwise.
   * @throws DescriptorWordException If the length of the segment is an invalid value or the buffer is not set.
   */
  public void setSegmentLength(int length, boolean extended) throws DescriptorWordException {

    this.checkSegmentLength(length, extended);

    if(extended) {
      this.getBuffer()[this.getOffset()] = (byte)(((length >> 24) & 0x7f) | 0x80);
      this.getBuffer()[this.getOffset() + 1] = (byte)((length >> 16) & 0xff);
      this.getBuffer()[this.getOffset() + 2] = (byte)((length >> 8) & 0xff);
      this.getBuffer()[this.getOffset() + 3] = (byte)(length & 0xff);
    } else {
      this.getBuffer()[this.getOffset()] = (byte)((length >> 8) & 0x7f);
      this.getBuffer()[this.getOffset() + 1] = (byte)(length & 0xff);
    }
  }
}
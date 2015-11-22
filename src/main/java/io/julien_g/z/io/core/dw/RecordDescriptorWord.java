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

/**
 * This class is used to read IBM RDW also
 * called SDW in case of spanned record.
 * 
 * The RDW is a 4 bytes length array.
 *
 * The two first bytes contain the length of the segment 
 * including the RDW using big endian endianess (max 32760). 
 *
 * The third byte contains a code about the position
 * of the segment into the record :
 * 00 - Complete logical record ;
 * 01 - First segment of a multisegment record ;
 * 10 - Last segment of a multisegment record ;
 * 11 - Segment of a multisegment record other than the first or last.
 *
 * The last byte is reserved for futur use and must be 0.
 */
public class RecordDescriptorWord extends DescriptorWord {

  /** Maximum length of a segment. */
  public final static int RDW_MAX_SEGMENT_LENGTH = 32760;

  /** Code for a complete record. */
  public final static byte RDW_CODE_COMPLETE_RECORD = 0x00;
  /** Code for the first part of a spanned record. */
  public final static byte RDW_CODE_FIRST_SEGMENT = 0x01;
  /** Code for the last part of a spanned record. */
  public final static byte RDW_CODE_LAST_SEGMENT = 0x02;
  /** Code for others parts of a spanned record. */
  public final static byte RDW_CODE_OTHER_SEGMENT = 0x03;

  /** Only acceptable value for the last byte of the RDW. */
  private final static byte RDW_LAST_BYTE_VALUE = 0x00;

  /** Possible record/segment descriptor word codes. */
  public enum Code {COMPLETE_RECORD, FIRST_SEGMENT, LAST_SEGMENT, OTHER_SEGMENT}


  /**
   * Default Constructor.
   */
  public RecordDescriptorWord() {
    super();
  }

  /**
   * Check the length of the record/segment.
   * @param length Length of the record/segment.
   * @throws A DescriptorWordException if the length of the segment is an invalid value.
   */
  private static void checkSegmentLength(int length) throws DescriptorWordException {

    if(length > RDW_MAX_SEGMENT_LENGTH || length < DescriptorWord.DESCRIPTOR_WORD_LENGTH) {
      throw new DescriptorWordException("Invalid segment length: "+length
        +". Length must be in the range ["+DescriptorWord.DESCRIPTOR_WORD_LENGTH+","+RDW_MAX_SEGMENT_LENGTH+"]");
    }

    return;
  }

  /**
   * Getter for the segment length.
   * @return A int containing the segment length.
   * @throws DescriptorWordException If the length of the segment is an invalid value or the buffer is not set.
   */
  public int getSegmentLength() throws DescriptorWordException {
    int length = (int)(this.getBuffer()[this.getOffset()] << 8 
      | (this.getBuffer()[this.getOffset() + 1] & 0xff));

    this.checkSegmentLength(length);

    return length;
  }

  /**
   * Setter for the segment length.
   * @param length Length of the record/segment to set.
   * @throws DescriptorWordException If the length of the segment is an invalid value or the buffer is not set.
   */
  public void setSegmentLength(int length) throws DescriptorWordException {
    this.checkSegmentLength(length);

    this.getBuffer()[this.getOffset()] = (byte)((length >> 8) & 0xff);
    this.getBuffer()[this.getOffset() + 1] = (byte)(length & 0xff);
  }

  /**
   * Getter for the code of the segment.
   * @return A byte containing the segment code.
   * @throws DescriptorWordException If the code of the segment is invalid or the buffer is not set.
   */
  public Code getSegmentCode() throws DescriptorWordException {
    byte bCode = this.getBuffer()[this.getOffset() + 2];
    Code code = Code.COMPLETE_RECORD;

    switch(bCode) {
      case RDW_CODE_COMPLETE_RECORD:
        code = Code.COMPLETE_RECORD;
        break;
      case RDW_CODE_FIRST_SEGMENT:
        code = Code.FIRST_SEGMENT;
        break;
      case RDW_CODE_LAST_SEGMENT:
        code = Code.LAST_SEGMENT;
        break;
      case RDW_CODE_OTHER_SEGMENT:
        code = Code.OTHER_SEGMENT;
        break;
      default:
        throw new DescriptorWordException("Invalid code: " + String.format("%02x", bCode)
          + ". Valid values are: ["+String.format("%02x", RDW_CODE_COMPLETE_RECORD)
          +","+String.format("%02x", RDW_CODE_FIRST_SEGMENT)
          +","+String.format("%02x", RDW_CODE_LAST_SEGMENT)
          +","+String.format("%02x", RDW_CODE_OTHER_SEGMENT)+"].");
    }

    return code;
  }

  /**
   * Setter for the code of the segment.
   * @param code Code of the record/segment descriptor word to set.
   * @throws DescriptorWordException If the code of the segment is invalid or the buffer is not set.
   */
  public void setSegmentCode(Code code) throws DescriptorWordException {

    byte bCode = 0;

    switch(code) {
      case COMPLETE_RECORD:
        bCode = RDW_CODE_COMPLETE_RECORD;
        break;
      case FIRST_SEGMENT:
        bCode = RDW_CODE_FIRST_SEGMENT;
        break;
      case LAST_SEGMENT:
        bCode = RDW_CODE_LAST_SEGMENT;
        break;
      case OTHER_SEGMENT:
        bCode = RDW_CODE_OTHER_SEGMENT;
        break;
      default:
        throw new DescriptorWordException("Unknow code: " + code.toString());
    }

    this.getBuffer()[this.getOffset() + 2] = bCode;
  }

  /**
   * Check if the last byte is valid.
   * @return True if the last byte is valid, false otherwise.
   * @throws DescriptorWordException if the last byte is invalid or the buffer is not set.
   */
  public boolean isLastByteValid() throws DescriptorWordException {
    return this.getBuffer()[this.getOffset() + 3] == RDW_LAST_BYTE_VALUE ?  true : false;
  }

  /**
   * Setter for the last byte of the record/segment descriptor word.
   * @throws DescriptorWordException if the buffer is not set.
   */
  public void setLastByte() throws DescriptorWordException {
    this.getBuffer()[this.getOffset() + 3] = RDW_LAST_BYTE_VALUE;
  }
	
}
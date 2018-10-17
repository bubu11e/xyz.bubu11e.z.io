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
 * A descriptor word is a four byte length array used
 * to describe records or blocks positions in an z/OS file.
 */
public class DescriptorWord {
	
  /** Size of a descriptor word in byte. */
  public final static int DESCRIPTOR_WORD_LENGTH = 4;

  /** Buffer containing the descriptor word. */
  protected byte[] buffer = null;
  /** Offset of the descriptor word in the buffer. */
  protected int offset = 0;

  public DescriptorWord() {
  }

  /**
   * Set the buffer containing the descriptor word.
   * @param buffer Buffer containing the descriptor word.
   * @param offset Offset of the descriptor word into the buffer.
   * @throws DescriptorWordException If the buffer size is invalid.
   */
  public void setBuffer(byte[] buffer, int offset) throws DescriptorWordException {
    if(buffer.length <= offset + DESCRIPTOR_WORD_LENGTH) {
      this.buffer = buffer;
      this.offset = offset;
    } else {
      throw new DescriptorWordException("Invalid buffer size.");
    }
  }

  /**
   * Set the buffer containing the descriptor word with a 0 offset.
   * @param buffer Buffer containing the descriptor word.
   * @throws DescriptorWordException If the buffer size is invalid.
   */
  public void setBuffer(byte[] buffer) throws DescriptorWordException {
    this.setBuffer(buffer, 0);
  }

  /**
   * Getter for the byte buffer.
   * @return The byte buffer.
   * @throws DescriptorWordException If the buffer has not been set.
   */
  public byte[] getBuffer() throws DescriptorWordException {
    if(buffer == null) {
      throw new DescriptorWordException("No buffer has been set !");
    }
    return this.buffer;
  }

  /**
   * Getter for the offset value.
   * @return An integer containing the offset value.
   */
  public int getOffset() {
    return this.offset;
  }
}
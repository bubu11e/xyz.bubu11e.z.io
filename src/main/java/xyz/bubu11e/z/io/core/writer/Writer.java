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

package xyz.bubu11e.z.io.core.writer;

import java.io.Closeable;
import java.io.IOException;

/**
 * Simple interface for z/OS filesystem
 * element writers.
 */
public interface Writer extends Closeable {

  /**
   * This function will write the specified object.
   * @param object Object to write.
   * @throws IOException If an I/O error occurs.
   * @throws WriterException If the specified object is invalid.
   */
  public void write(byte[] object) throws IOException, WriterException;

}
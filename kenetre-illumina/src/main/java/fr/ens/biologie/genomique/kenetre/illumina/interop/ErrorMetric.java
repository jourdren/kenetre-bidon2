/*
 *                  Aozan development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU General Public License version 3 or later 
 * and CeCILL. This should be distributed with the code. If you 
 * do not have a copy, see:
 *
 *      http://www.gnu.org/licenses/gpl-3.0-standalone.html
 *      http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 *
 * Copyright for this code is held jointly by the Genomic platform
 * of the Institut de Biologie de l'École Normale Supérieure and
 * the individual authors. These should be listed in @author doc
 * comments.
 *
 * For more information on the Aozan project and its aims,
 * or to join the Aozan Google group, visit the home page at:
 *
 *      http://outils.genomique.biologie.ens.fr/aozan
 *
 */

package fr.ens.biologie.genomique.kenetre.illumina.interop;

import static fr.ens.biologie.genomique.kenetre.illumina.interop.AbstractBinaryFileReader.uIntToLong;
import static fr.ens.biologie.genomique.kenetre.illumina.interop.AbstractBinaryFileReader.uShortToInt;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * This internal class save a record from ErrorMetricsOut.bin file,
 * corresponding of the description of the EXPECTED_VERSION. An record contains
 * data per tile per cycle per lane. Each record create an object
 * IlluminaErrorMetrics.______________________________________________________
 * byte 0: file version number (3)____________________________________________
 * byte 1: length of each record______________________________________________
 * bytes (N * 30 + 2) - (N *30 + 11): record:_________________________________
 * __2 bytes: lane number (uint16)____________________________________________
 * __2 bytes: tile number (uint16)____________________________________________
 * __2 bytes: cycle number (uint16)___________________________________________
 * __4 bytes: error rate (float)______________________________________________
 * __4 bytes: number of perfect countReads (uint32)___________________________
 * __4 bytes: number of countReads with 1 error (uint32)______________________
 * __4 bytes: number of countReads with 2 errors (uint32)_____________________
 * __4 bytes: number of countReads with 3 errors (uint32)_____________________
 * __4 bytes: number of countReads with 4 errors (uint32)_____________________
 * Where N is the record index________________________________________________
 * @author Sandrine Perrin
 * @since Aozan 1.1
 */
public class ErrorMetric extends Metric {

  /** The lane number. */
  private final int laneNumber;

  /** The tile number. */
  private final long tileNumber;

  /** The cycle number. */
  private final int cycleNumber;

  /** The error rate. */
  private final float errorRate;

  /** The number perfect reads. */
  private final int numberPerfectReads;

  /** The number reads one error. */
  private final int numberReadsOneError;

  /** The number reads two errors. */
  private final int numberReadsTwoErrors;

  /** The number reads three errors. */
  private final int numberReadsThreeErrors;

  /** The number reads four errors. */
  private final int numberReadsFourErrors;

  private final float fractionOfReadAdapterTrimmed;

  private final float[] fractionOfReadAdapterTrimmedArray;

  private final List<String> adapterSequences;

  /**
   * Get the number lane.
   * @return the lane number
   */
  public int getLaneNumber() {
    return this.laneNumber;
  }

  /**
   * Get the number tile.
   * @return the tile number
   */
  public long getTileNumber() {
    return this.tileNumber;
  }

  /**
   * Get the number cycle of this record.
   * @return number cycle
   */
  public int getCycleNumber() {
    return this.cycleNumber;
  }

  /**
   * Get the rate error of this record.
   * @return rate error
   */
  public float getErrorRate() {
    return this.errorRate;
  }

  /**
   * Get the number perfect countReads for this record.
   * @return number perfect countReads
   */
  public int getNumberPerfectReads() {
    return this.numberPerfectReads;
  }

  /**
   * Gets the number reads one error.
   * @return the number reads one error
   */
  public int getNumberReadsOneError() {
    return this.numberReadsOneError;
  }

  /**
   * Gets the number reads two errors.
   * @return the number reads two errors
   */
  public int getNumberReadsTwoErrors() {
    return this.numberReadsTwoErrors;
  }

  /**
   * Gets the number reads three errors.
   * @return the number reads three errors
   */
  public int getNumberReadsThreeErrors() {
    return this.numberReadsThreeErrors;
  }

  /**
   * Gets the number reads four errors.
   * @return the number reads four errors
   */
  public int getNumberReadsFourErrors() {
    return this.numberReadsFourErrors;
  }

  /**
   * Get the Phix adapter rate.
   * @return the Phix adapter rate
   */
  public float getPhiXAdapterRate() {
    return this.fractionOfReadAdapterTrimmed;
  }

  /**
   * Get the number of adapters.
   * @return the number of adapters
   */
  public int adapterCount() {

    return this.adapterSequences.size();
  }

  /**
   * Get an adapter sequence.
   * @param adapterIndex the index of the adapter
   * @return the requested adapter sequence
   */
  public String getAdapterSequence(int adapterIndex) {

    return this.adapterSequences.get(adapterIndex);
  }

  /**
   * Get an adapter rate.
   * @param adapterIndex the index of the adapter
   * @return the requested adapter rate
   */
  public float getAdapterRate(int adapterIndex) {

    return this.fractionOfReadAdapterTrimmedArray[adapterIndex];
  }

  //
  // Metric methods
  //

  @Override
  public List<String> fieldNames() {

    return Arrays.asList("Lane", "Tile", "Cycle", "ErrorRate",
        "PhiXAdapterRate");
  }

  @Override
  public List<Class<?>> fieldTypes() {

    return Arrays.asList(Integer.class, Integer.class, Integer.class,
        Float.class, Float.class);
  }

  @Override
  public List<Number> values() {

    return Arrays.asList(getLaneNumber(), getTileNumber(), getCycleNumber(),
        getErrorRate(), this.fractionOfReadAdapterTrimmed);
  }

  //
  // Object methods
  //

  @Override
  public String toString() {
    return String.format("%s\t%s\t%s\t%.2f\t%s\t%s\t%s\t%s\t%s",
        this.laneNumber, this.tileNumber, this.cycleNumber, this.errorRate,
        this.numberPerfectReads, this.numberReadsOneError,
        this.numberReadsTwoErrors, this.numberReadsThreeErrors,
        this.numberReadsFourErrors);
  }

  //
  // Constructor
  //

  /**
   * Constructor. One record countReads on the ByteBuffer.
   * @param bb ByteBuffer who read one record
   */
  ErrorMetric(final int version, final List<String> adapterSequences,
      final ByteBuffer bb) {

    super.name = "Error";
    super.version = version;

    this.laneNumber = uShortToInt(bb);
    this.tileNumber = version > 3 ? uIntToLong(bb) : uShortToInt(bb);
    this.cycleNumber = uShortToInt(bb);

    this.errorRate = bb.getFloat();

    switch (version) {

    case 3:
      this.numberPerfectReads = bb.getInt();

      this.numberReadsOneError = bb.getInt();
      this.numberReadsTwoErrors = bb.getInt();
      this.numberReadsThreeErrors = bb.getInt();
      this.numberReadsFourErrors = bb.getInt();
      this.fractionOfReadAdapterTrimmed = Float.NaN;
      this.fractionOfReadAdapterTrimmedArray = null;
      this.adapterSequences = null;
      break;

    case 4:
      this.numberPerfectReads = -1;
      this.numberReadsOneError = -1;
      this.numberReadsTwoErrors = -1;
      this.numberReadsThreeErrors = -1;
      this.numberReadsFourErrors = -1;
      this.fractionOfReadAdapterTrimmed = Float.NaN;
      this.fractionOfReadAdapterTrimmedArray = null;
      this.adapterSequences = null;
      break;

    case 5:
      this.numberPerfectReads = -1;
      this.numberReadsOneError = -1;
      this.numberReadsTwoErrors = -1;
      this.numberReadsThreeErrors = -1;
      this.numberReadsFourErrors = -1;
      this.fractionOfReadAdapterTrimmed = bb.getFloat();
      this.fractionOfReadAdapterTrimmedArray = null;
      this.adapterSequences = null;
      break;

    case 6:
      this.numberPerfectReads = -1;
      this.numberReadsOneError = -1;
      this.numberReadsTwoErrors = -1;
      this.numberReadsThreeErrors = -1;
      this.numberReadsFourErrors = -1;
      this.fractionOfReadAdapterTrimmed = Float.NaN;
      this.adapterSequences = adapterSequences;
      int numAdapter = adapterSequences.size();
      this.fractionOfReadAdapterTrimmedArray = new float[numAdapter];

      for (int i = 0; i < numAdapter; i++) {
        this.fractionOfReadAdapterTrimmedArray[i] = bb.getFloat();
      }
      break;

    default:
      throw new IllegalStateException("Unknown version: " + version);
    }

  }

}

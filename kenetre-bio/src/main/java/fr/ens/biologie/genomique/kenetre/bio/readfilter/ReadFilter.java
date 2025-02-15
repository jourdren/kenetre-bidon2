/*
 *                  Eoulsan development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public License version 2.1 or
 * later and CeCILL-C. This should be distributed with the code.
 * If you do not have a copy, see:
 *
 *      http://www.gnu.org/licenses/lgpl-2.1.txt
 *      http://www.cecill.info/licences/Licence_CeCILL-C_V1-en.txt
 *
 * Copyright for this code is held jointly by the Genomic platform
 * of the Institut de Biologie de l'École normale supérieure and
 * the individual authors. These should be listed in @author doc
 * comments.
 *
 * For more information on the Eoulsan project and its aims,
 * or to join the Eoulsan Google group, visit the home page
 * at:
 *
 *      http://outils.genomique.biologie.ens.fr/eoulsan
 *
 */

package fr.ens.biologie.genomique.kenetre.bio.readfilter;

import fr.ens.biologie.genomique.kenetre.log.GenericLogger;
import fr.ens.biologie.genomique.kenetre.KenetreException;
import fr.ens.biologie.genomique.kenetre.bio.ReadSequence;

/**
 * This interface define a filter for reads.
 * @since 1.0
 * @author Laurent Jourdren
 */
public interface ReadFilter {

  /**
   * Tests if a specified read should be keep.
   * @param read read to test
   * @return true if the the read sequence must be kept
   */
  boolean accept(ReadSequence read);

  /**
   * Tests if the specified reads should be keep.
   * @param read1 first read to test
   * @param read2 second read to test
   * @return true if the the read sequence must be kept
   */
  boolean accept(ReadSequence read1, ReadSequence read2);

  /**
   * Get the name of the filter.
   * @return the name of the filter
   */
  String getName();

  /**
   * Get the description of the filter.
   * @return the description of the filter
   */
  String getDescription();

  /**
   * Set a parameter of the ReadFilter.
   * @param key name of the parameter to set
   * @param value value of the parameter to set
   * @throws KenetreException if the parameter is invalid
   */
  void setParameter(String key, String value) throws KenetreException;

  /**
   * Set the logger to use.
   * @param logger the logger to use
   */
  void setLogger(GenericLogger logger);

  /**
   * Get the logger.
   * @return the logger
   */
  GenericLogger getLogger();

  /**
   * Initialize the filter.
   * @throws KenetreException an error occurs while initialize the filter
   */
  void init() throws KenetreException;

}

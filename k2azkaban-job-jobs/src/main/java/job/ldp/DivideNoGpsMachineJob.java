package job.ldp;

import com.k2data.platform.persistence.SqlRunner;
import com.k2data.platform.persistence.transaction.TransactionUtils;

/**
 * @author lidong 16-12-12.
 */
public class DivideNoGpsMachineJob {

    public void run() {
        String updateGpsNoSql = "UPDATE lg_machineProfile b SET b.gpsNo = (SELECT a.gpsNo FROM lg_machineGpsContrast a WHERE a.deviceNo = b.deviceNo)";

        String deleteNoGpsTableSql = "DELETE FROM lg_machineProfileNoGps";

        String insertNoGpsData2NoGpsTableSql = "INSERT INTO lg_machineProfileNoGps" +
                "        (id," +
                "         deviceNo," +
                "         pincode," +
                "         shortname," +
                "         name," +
                "         enname," +
                "         modelnumber," +
                "         ordernumber," +
                "         machinetype," +
                "         certificationnumber," +
                "         machinestatus," +
                "         statusdetail," +
                "         productdate," +
                "         servicefilenumber," +
                "         filecreatedate," +
                "         bookbuildingid," +
                "         saleareaid," +
                "         saletype," +
                "         saleunitid," +
                "         saledate," +
                "         customerid," +
                "         productfactoryid," +
                "         batchnumber," +
                "         producttype," +
                "         smartterminalornot," +
                "         transmitcode," +
                "         stopservicemark," +
                "         imgUrl," +
                "         labMark," +
                "         labStartTime," +
                "         labEndTime," +
                "         jobWorkState," +
                "         environmentWorkState," +
                "         paymentType," +
                "         storehouseId," +
                "         paymentNum" +
                "        )" +
                "        SELECT" +
                "            a.id AS \"id\"," +
                "            a.deviceNo AS \"deviceNo\"," +
                "            a.pincode AS \"pinCode\"," +
                "            a.shortname AS \"shortName\"," +
                "            a.name AS \"name\"," +
                "            a.enname AS \"enName\"," +
                "            a.modelnumber AS \"modelNumber\"," +
                "            a.ordernumber AS \"orderNumber\"," +
                "            a.machinetype AS \"machineType\"," +
                "            a.certificationnumber AS \"certificationNumber\"," +
                "            a.machinestatus AS \"machineStatus\"," +
                "            a.statusdetail AS \"statusDetail\"," +
                "            a.productdate AS \"productDate\"," +
                "            a.servicefilenumber AS \"serviceFileNumber\"," +
                "            a.filecreatedate AS \"fileCreatedate\"," +
                "            a.bookbuildingid AS \"bookBuildingId\"," +
                "            a.saleareaid AS \"saleAreaId\"," +
                "            a.saletype AS \"saleType\"," +
                "            a.saleunitid AS \"saleUnitId\"," +
                "            a.saledate AS \"saleDate\"," +
                "            a.customerid AS \"customerId\"," +
                "            a.productfactoryid AS \"productFactoryId\"," +
                "            a.batchnumber AS \"batchNumber\"," +
                "            a.producttype AS \"productType\"," +
                "            a.smartterminalornot AS \"smartTerminalOrNot\"," +
                "            a.transmitcode AS \"transmitCode\"," +
                "            a.stopservicemark AS \"stopServiceMark\"," +
                "            a.imgUrl AS \"imgUrl\"," +
                "            a.labMark AS \"labMark\"," +
                "            a.labStartTime AS \"labStartTime\"," +
                "            a.labEndTime AS \"labEndTime\"," +
                "            a.jobWorkState AS \"jobWorkState\"," +
                "            a.environmentWorkState AS \"environmentWorkState\"," +
                "            a.paymentType AS \"paymentType\"," +
                "            a.storehouseId AS \"storehouseId\"," +
                "            a.paymentNum AS \"paymentNum\"" +
                "        FROM lg_machineProfile a" +
                "        WHERE a.gpsNo IS NULL";

        String deleteNoGpsDataSql = "DELETE FROM lg_machineProfile WHERE gpsNo IS NULL";

        try {
            TransactionUtils.beginTransaction();

            SqlRunner.update(updateGpsNoSql);
            SqlRunner.update(deleteNoGpsTableSql);
            SqlRunner.update(insertNoGpsData2NoGpsTableSql);
            SqlRunner.update(deleteNoGpsDataSql);

            TransactionUtils.commitTransaction();
        } catch (Exception e) {
            TransactionUtils.rollbackTransaction();
            throw new RuntimeException(e);
        } finally {
            TransactionUtils.closeConnection();
        }
    }

}

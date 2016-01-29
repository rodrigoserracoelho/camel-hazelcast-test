package com.camel.test.file;

import org.apache.camel.Exchange;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileEndpoint;
import org.apache.camel.component.file.GenericFileOperationFailedException;
import org.apache.camel.component.file.GenericFileOperations;
import org.apache.camel.component.file.strategy.GenericFileProcessStrategySupport;

import com.camel.test.hazelcast.TestCamelHazelCast;

public class FileStrategy<T> extends GenericFileProcessStrategySupport<T> {

	public void prepareOnStartup(GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint)
			throws Exception {
		super.prepareOnStartup(operations, endpoint);

	}

	public boolean begin(GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint, Exchange exchange,
			GenericFile<T> file) throws Exception {
		return super.begin(operations, endpoint, exchange, file);
	}

	public void abort(GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint, Exchange exchange,
			GenericFile<T> file) throws Exception {
		super.abort(operations, endpoint, exchange, file);
	}

	public void commit(GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint, Exchange exchange,
			GenericFile<T> file) throws Exception {

		GenericFile<T> result = file.copyFrom(file);
		String newName = exchange.getIn().getHeader("CamelFileParent") + "/.camel/"
				+ exchange.getIn().getHeader("CamelFileNameOnly");
		result.changeFileName(newName);

		// deleting any existing files before renaming
		try {
			operations.deleteFile(result.getAbsoluteFilePath());
		} catch (GenericFileOperationFailedException e) {
			// ignore the file does not exists
		}

		// make parent folder if missing
		boolean mkdir = operations.buildDirectory(result.getParent(), result.isAbsolute());

		boolean renamed = operations.renameFile(file.getAbsoluteFilePath(), result.getAbsoluteFilePath());

		if (!renamed) {
			System.out.println("------------------------------->FAILED RENAMING WE DONT NEED TO REMOVE FROM CACHE :"
					+ exchange.isFailed());
			// throw new GenericFileOperationFailedException("Cannot rename
			// file: " + file + " to: " + result);
		} else {
			TestCamelHazelCast fileLocker = TestCamelHazelCast.getFileLocker();
			fileLocker.remove(file.getFileName());
		}
		super.commit(operations, endpoint, exchange, file);

	}

	public void rollback(GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint, Exchange exchange,
			GenericFile<T> file) throws Exception {
		// TODO Auto-generated method stub
		super.rollback(operations, endpoint, exchange, file);
	}

}

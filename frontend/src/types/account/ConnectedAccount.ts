import type {AbstractEntity} from "@/types/AbstractEntity";

export interface ConnectedAccount extends AbstractEntity {
    connectedAt: string;
    provider: string;
    subject: string;
}
